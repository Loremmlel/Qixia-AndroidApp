package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.hinanawiyuzu.qixia.data.entity.MedicineFrequency
import org.hinanawiyuzu.qixia.data.entity.MedicineRemind
import org.hinanawiyuzu.qixia.data.entity.MedicineRepo
import org.hinanawiyuzu.qixia.data.entity.TakeMethod
import org.hinanawiyuzu.qixia.data.entity.toMedicineFrequency
import org.hinanawiyuzu.qixia.data.repo.MedicineRemindRepository
import org.hinanawiyuzu.qixia.data.repo.MedicineRepoRepository
import org.hinanawiyuzu.qixia.utils.RemindRoute
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

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
    var remindTime: LocalTime? by mutableStateOf(null)
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
            isTaken = false,
            medicineRepoId = medicineRepoId!!,
            method = method!!
        )
        viewModelScope.launch{
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