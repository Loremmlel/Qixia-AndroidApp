package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.hinanawiyuzu.qixia.QixiaApplication
import org.hinanawiyuzu.qixia.data.entity.IsTake
import org.hinanawiyuzu.qixia.data.entity.isTakenInSpecificDate
import org.hinanawiyuzu.qixia.data.repo.MedicineRemindRepository
import java.time.LocalDate
import java.time.LocalDateTime

class RecordViewModel(
    medicineRemindRepository: MedicineRemindRepository,
    application: QixiaApplication
) : ViewModel() {
    val currentLoginUserId = application.currentLoginUserId
    private val allMedicineRemind: StateFlow<AllMedicineRemind> =
        medicineRemindRepository.getAllStream()
            .map { allMedicineRemind ->
                AllMedicineRemind(allMedicineRemind.filter { it.userId == application.currentLoginUserId })
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = AllMedicineRemind()
            )
    var takeMedicineRecord: List<TakeMedicineRecord> by mutableStateOf(emptyList())

    init {
        viewModelScope.launch {
            val currentDate = LocalDate.now()
            allMedicineRemind.collect {
                takeMedicineRecord =
                    (it.medicineRemindList.map { medicineRemind ->
                        TakeMedicineRecord(
                            medicineName = medicineRemind.name,
                            takeStatus = medicineRemind.isTakenInSpecificDate(currentDate)
                        )
                    } + it.medicineRemindList.map { medicineRemind ->
                        TakeMedicineRecord(
                            medicineName = medicineRemind.name,
                            takeStatus = medicineRemind.isTakenInSpecificDate(currentDate.minusDays(1))
                        )
                    } + it.medicineRemindList.map { medicineRemind ->
                        TakeMedicineRecord(
                            medicineName = medicineRemind.name,
                            takeStatus = medicineRemind.isTakenInSpecificDate(currentDate.minusDays(2))
                        )
                    } + it.medicineRemindList.map { medicineRemind ->
                        TakeMedicineRecord(
                            medicineName = medicineRemind.name,
                            takeStatus = medicineRemind.isTakenInSpecificDate(currentDate.minusDays(3))
                        )
                    }).filter { takeMedicineRecord -> takeMedicineRecord.takeStatus.first != IsTake.NotNeed }
            }
        }
    }
}

data class TakeMedicineRecord(
    val medicineName: String,
    val takeStatus: Pair<IsTake, LocalDateTime?>
)