package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.navigation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.data.repo.*
import org.hinanawiyuzu.qixia.utils.*
import java.time.*
import java.time.temporal.*

class NewRemindViewModel(
    private val medicineRemindRepository: MedicineRemindRepository,
    private val medicineRepoRepository: MedicineRepoRepository
) : ViewModel() {
    var medicineRepoId: Int? by mutableStateOf(null)
    private var medicineRepo: MedicineRepo? by mutableStateOf(null)
    var medicineName: String by mutableStateOf("")
    var dose: String? by mutableStateOf(null)
    var frequency: MedicineFrequency? by mutableStateOf(null)
    var startDate: LocalDate? by mutableStateOf(null)
    var endDate: LocalDate? by mutableStateOf(null)
    private var remindTime: LocalTime? by mutableStateOf(null)
    var method: TakeMethod? by mutableStateOf(null)
    var buttonEnabled: Boolean by mutableStateOf(false)

    fun onSelectMedicineFromBoxClicked(navController: NavController) {
        navController.navigate(RemindRoute.MedicineRepoScreen.name)
    }

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
            0 -> TakeMethod.BeforeMeal
            1 -> TakeMethod.AtMeal
            2 -> TakeMethod.AfterMeal
            else -> null
        }
        checkButtonEnabled()
    }

    private fun checkButtonEnabled() {
        buttonEnabled =
            dose != null && frequency != null && startDate != null && endDate != null && remindTime != null && method != null
    }
}