package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.navigation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.data.repo.*
import org.hinanawiyuzu.qixia.ui.screen.*
import org.hinanawiyuzu.qixia.utils.*
import java.time.*
import java.time.temporal.*

class NewRemindViewModel(
    private val medicineRemindRepository: MedicineRemindRepository,
    private val medicineRepoRepository: MedicineRepoRepository
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
     */
    fun onCommitButtonClicked(navController: NavController) {
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
        viewModelScope.launch {
            medicineRemindRepository.insertMedicineRemind(medicineRemind)
            navController.popBackStack()
        }
    }

    /**
     * 根据仓库id获取对应的药品信息
     */
    fun getMedicineRepo() {
        viewModelScope.launch {
            medicineRepo = medicineRepoRepository.getMedicineRepoStreamById(medicineRepoId!!).firstOrNull()
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
        startDate = millis?.let {
            Instant
                .ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
        if (endDate != null && startDate != null) {
            // 如果用户已经选择了结束日期，但是选择的开始日期又大于结束日期的话，那么将结束日期置空，让用户重新选择。
            if (startDate!! >= endDate!!) {
                endDate = null
            }
        }
        checkButtonEnabled()
    }

    fun onEndDatePickerConfirmButtonClicked(millis: Long?) {
        endDate = millis?.let {
            Instant
                .ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
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