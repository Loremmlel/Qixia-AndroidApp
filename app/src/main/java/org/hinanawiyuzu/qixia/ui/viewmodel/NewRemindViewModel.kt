package org.hinanawiyuzu.qixia.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.hinanawiyuzu.qixia.GlobalValues
import org.hinanawiyuzu.qixia.QixiaApplication
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.data.repo.AlarmDateTimeRepository
import org.hinanawiyuzu.qixia.data.repo.MedicineRemindRepository
import org.hinanawiyuzu.qixia.data.repo.MedicineRepoRepository
import org.hinanawiyuzu.qixia.exception.AlarmSetFailedException
import org.hinanawiyuzu.qixia.notification.Schedule
import org.hinanawiyuzu.qixia.ui.route.RemindRoute
import org.hinanawiyuzu.qixia.ui.screen.MedicineRepoScreen
import org.hinanawiyuzu.qixia.utils.showLongToast
import org.hinanawiyuzu.qixia.utils.toLocalDate
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class NewRemindViewModel(
    private val medicineRemindRepository: MedicineRemindRepository,
    private val medicineRepoRepository: MedicineRepoRepository,
    private val alarmDateTimeRepository: AlarmDateTimeRepository,
    private val application: QixiaApplication
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
    private var remindTimes: List<LocalTime?> by mutableStateOf(List(6) { null })

    // 服药方式(饭前、饭中、饭后等)
    var method: TakeMethod? by mutableStateOf(null)
        private set

    // 提交按钮是否可用
    var buttonEnabled: Boolean by mutableStateOf(false)
        private set

    // 提醒时间有几个需要设置
    var remindTimeSelectorAmount: Int by mutableIntStateOf(1)

    var intervalDays: Int by mutableIntStateOf(1)

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
        if (!hasPermission)
            showLongToast(context, "不开启通知权限的话，无法正常使用提醒功能。")
        val currentLoginUserId = application.currentLoginUserId!!
        // 计算总天数
        val totalDays = (ChronoUnit.DAYS.between(startDate!!, endDate!!) + 1).toInt()
        // 计算提醒次数
        val times = (totalDays - 1) / intervalDays + 1
        val medicineReminds = emptyList<MedicineRemind>().toMutableList()
        val alarmDateTimes = emptyList<AlarmDateTime>().toMutableList()
        // PendingIntent的requestCode
        val requestCodes: List<List<Int>> =
            List(remindTimeSelectorAmount) { List(times) { (0..Int.MAX_VALUE).random() } }
        for (i in (0..<remindTimeSelectorAmount)) {
            medicineReminds.add(
                MedicineRemind(
                    remindTime = remindTimes[i]!!,
                    startDate = startDate!!,
                    endDate = endDate!!,
                    name = medicineName,
                    dose = dose!!,
                    frequency = frequency!!,
                    isTaken = List(times) { false },
                    medicineRepoId = medicineRepoId!!,
                    method = method!!,
                    userId = currentLoginUserId
                )
            )
        }
        viewModelScope.launch {
            medicineReminds.forEachIndexed { index, remind ->
                alarmDateTimes.add(
                    AlarmDateTime(
                        remindId = medicineRemindRepository.insertAndGetId(remind).toInt(),
                        type = GlobalValues.TAKE_MEDICINE_REMIND,
                        startDateTime = startDate!!.atTime(remindTimes[index]),
                        endDateTime = endDate!!.atTime(remindTimes[index]),
                        frequency = frequency!!,
                        requestCode = requestCodes[index]
                    )
                )
            }
            val schedule = Schedule(context)
            try {
                schedule.setTakeMedicineAlarm(medicineReminds.toList(), requestCodes, currentLoginUserId)
                alarmDateTimes.forEach { alarmDateTimeRepository.insert(it) }
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
                alarmDateTimes.forEach { alarmDateTimeRepository.delete(it) }
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
        remindTimeSelectorAmount = when (frequency) {
            MedicineFrequency.TwiceDaily -> 2
            MedicineFrequency.ThreeTimesDaily -> 3
            MedicineFrequency.FourTimesDaily -> 4
            MedicineFrequency.FiveTimesDaily -> 5
            MedicineFrequency.SixTimesDaily -> 6
            else -> 1
        }
        intervalDays = when (frequency) {
            MedicineFrequency.OnceTwoDays -> 2
            MedicineFrequency.OnceAWeek -> 7
            MedicineFrequency.OnceTwoWeeks -> 14
            MedicineFrequency.OnceAMonth -> 30
            else -> 1
        }
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

    fun onRemindTimeSelected(index: Int, hours: Int, minutes: Int) {
        val newRemindTimes = remindTimes.toMutableList().apply { set(index, LocalTime.of(hours, minutes)) }
        remindTimes = newRemindTimes.toList()
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
        // 用户是否选择了提醒时间
        var flag = false
        remindTimes.forEach {
            if (it != null) {
                flag = true
                return@forEach
            }
        }
        buttonEnabled =
            !dose.isNullOrEmpty() &&
                    medicineName.isNotEmpty() &&
                    frequency != null &&
                    startDate != null &&
                    endDate != null &&
                    method != null && flag
    }
}