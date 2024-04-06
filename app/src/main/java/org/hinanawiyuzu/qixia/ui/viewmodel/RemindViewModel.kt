package org.hinanawiyuzu.qixia.ui.viewmodel

import android.net.*
import androidx.compose.runtime.*
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.data.repo.*
import java.time.*

class RemindViewModel(
    medicineRemindRepository: MedicineRemindRepository,
    medicineRepoRepository: MedicineRepoRepository
) : ViewModel() {
    var currentSelectedDate: LocalDate by mutableStateOf(LocalDate.now())
    var allMedicineRemind: StateFlow<AllMedicineRemind> =
        medicineRemindRepository.getAllMedicineRemindsStream()
            .map { AllMedicineRemind(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = AllMedicineRemind()
            )
    var allMedicineRepo: StateFlow<AllMedicineRepo> =
        medicineRepoRepository.getAllMedicineRepoStream()
            .map { AllMedicineRepo(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = AllMedicineRepo()
            )

    fun onCalendarClicked(index: Int) {
        currentSelectedDate = LocalDate.now().plusDays((index - 15).toLong())
    }

    fun searchImageFromMedicineRepo(reminds: MedicineRemind): Uri {
        return allMedicineRepo.value.allMedicineRepoList.find { it.id == reminds.medicineRepoId }?.imageUri ?: Uri.EMPTY
    }

}

data class AllMedicineRemind(
    val medicineRemindList: List<MedicineRemind> = emptyList()
)


