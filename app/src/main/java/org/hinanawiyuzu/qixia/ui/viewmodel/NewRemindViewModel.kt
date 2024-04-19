package org.hinanawiyuzu.qixia.ui.viewmodel

import android.content.*
import android.os.*
import android.provider.*
import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.navigation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.*
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.data.repo.*
import org.hinanawiyuzu.qixia.exception.*
import org.hinanawiyuzu.qixia.notification.*
import org.hinanawiyuzu.qixia.ui.screen.*
import org.hinanawiyuzu.qixia.utils.*
import java.time.*
import java.time.temporal.*

class NewRemindViewModel(
    private val medicineRemindRepository: MedicineRemindRepository,
    private val medicineRepoRepository: MedicineRepoRepository,
    private val alarmDateTimeRepository: AlarmDateTimeRepository
) : ViewModel() {
    /**
     *对应的仓库id，从[MedicineRepoScreen]传过来
     */
    var medicineRepoId: Int? by mutableStateOf(null)

    /**
     * 根据仓库id查到的药品信息
     */
    private var medicineRepo: MedicineRepo? by mutableStateOf(null)
    var medicineName: String by mutableStateOf("")
        private set

    // 剂量
    var dose: String? by mutableStateOf(null)
        private set

    // 服药频率
    var frequency: MedicineFrequency? by mutableStateOf(null)
        private set

    // 提醒开始日期
    var startDate: LocalDate? by mutableStateOf(null)
        private set

    // 提醒结束日期
    var endDate: LocalDate? by mutableStateOf(null)
        private set

    // 提醒时间
    private var remindTime: LocalTime? by mutableStateOf(null)

    // 服药方式(饭前、饭中、饭后等)
    var method: TakeMethod? by mutableStateOf(null)
        private set

    // 提交按钮是否可用
    var buttonEnabled: Boolean by mutableStateOf(false)
        private set

    fun onSelectMedicineFromBoxClicked(navController: NavController) {
        navController.navigate(RemindRoute.MedicineRepoScreen.name)
    }

    /**
     * 提交按钮点击事件
     * 提交数据到数据库的同时返回上一个页面
     * @param navController 导航控制器,用于返回上一个页面
     * @param hasPermission 是否有通知权限
     */
    fun onCommitButtonClicked(
        context: Context,
        navController: NavController,
        hasPermission: Boolean
    ) {
        val medicineRemind = MedicineRemind(
            remindTime = remindTime!!,
            startDate = startDate!!,
            endDate = endDate!!,
            name = medicineName,
            dose = dose!!,
            frequency = frequency!!,
            isTaken = List((ChronoUnit.DAYS.between(startDate!!, endDate!!) + 1).toInt()) { false },
            medicineRepoId = medicineRepoId!!,
            method = method!!
        )
        if (!hasPermission) {
            showLongToast(context, "不开启通知权限的话，无法正常使用提醒功能。")
        }
        viewModelScope.launch {
            val remindId = medicineRemindRepository.insertAndGetId(medicineRemind).toInt()
            // TODO: 提早五分钟提醒,这个后期应当在设置中由用户自行选择。
            val startTimeMillis = startDate!!.atTime(remindTime).toEpochMillis() - 300000
            val days = (ChronoUnit.DAYS.between(startDate, endDate) + 1).toInt()
            val schedule = Schedule(context)
            // 提醒正文
            val remindContent =
                "该服药了!请在${remindTime}之前服用${medicineName}  ${dose}片,注意${method!!.convertToString()}服用"
            // PendingIntent的requestCode
            val requestCodes: List<Int> = List(days) { (0..Int.MAX_VALUE).random() }
            val alarmDateTime = AlarmDateTime(
                remindId = remindId,
                type = GlobalValues.TAKE_MEDICINE_REMIND,
                remindContent = remindContent,
                startDateTime = startTimeMillis.toLocalDateTime(),
                endDateTime = (startTimeMillis + (days - 1) * 86400000).toLocalDateTime(),
                requestCode = requestCodes
            )
            try {
                schedule.setTakeMedicineAlarm(remindId, remindContent, startTimeMillis, days, requestCodes)
                alarmDateTimeRepository.insert(alarmDateTime)
            } catch (e: SecurityException) {
                showLongToast(context, "请开启精确闹钟权限")
                if (Build.VERSION.SDK_INT >= 31) {
                    val intent = Intent().apply {
                        action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    }
                    context.startActivity(intent)
                }
            } catch (e: AlarmSetFailedException) {
                showLongToast(context, "闹钟设置失败")
            } catch (e: Exception) {
                alarmDateTimeRepository.delete(alarmDateTime)
                showLongToast(context, "提醒设置失败")
            }
            navController.popBackStack()
        }
    }

    /**
     * 根据仓库id获取对应的药品信息
     */
    fun getMedicineRepo() {
        viewModelScope.launch {
            medicineRepo = medicineRepoRepository.getStreamById(medicineRepoId!!).firstOrNull()
            medicineName = medicineRepo!!.name
        }
    }

    fun onDoseDropDownMenuItemClicked(value: String) {
        dose = value
        checkButtonEnabled()
    }

    fun onFrequencyDropDownMenuItemClicked(value: String) {
        frequency = value.toMedicineFrequency()
        checkButtonEnabled()
    }

    /**
     * 开始日期选择器确认按钮点击事件
     * 逻辑有：如果用户选择了结束日期，但是选择的开始日期又大于结束日期的话，那么将结束日期置空，让用户重新选择。
     * @param millis 选择的日期的时间戳
     * @author HinanawiYuzu
     */
    fun onStartDatePickerConfirmButtonClicked(millis: Long?) {
        startDate = millis?.toLocalDate()
        if (endDate != null && startDate != null) {
            // 如果用户已经选择了结束日期，但是选择的开始日期又大于结束日期的话，那么将结束日期置空，让用户重新选择。
            if (startDate!! >= endDate!!) {
                endDate = null
            }
        }
        checkButtonEnabled()
    }

    fun onEndDatePickerConfirmButtonClicked(millis: Long?) {
        endDate = millis?.toLocalDate()
        checkButtonEnabled()
    }

    fun onRemindTimeSelected(hours: Int, minutes: Int) {
        remindTime = LocalTime.of(hours, minutes)
        checkButtonEnabled()
    }

    fun onSelectMethodClicked(methodId: Int) {
        method = when (methodId) {
            // ui设计只提供了饭前、饭中和饭后三个选项……前后矛盾的设计。
            0 -> TakeMethod.BeforeMeal
            1 -> TakeMethod.AtMeal
            2 -> TakeMethod.AfterMeal
            else -> null
        }
        checkButtonEnabled()
    }

    /**
     * 检查按钮是否可用。
     */
    private fun checkButtonEnabled() {
        buttonEnabled =
            !dose.isNullOrEmpty() &&
                    medicineName.isNotEmpty() &&
                    frequency != null &&
                    startDate != null &&
                    endDate != null &&
                    remindTime != null &&
                    method != null
    }
}