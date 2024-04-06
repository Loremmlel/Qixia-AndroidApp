package org.hinanawiyuzu.qixia.ui.viewmodel.shared

import androidx.compose.runtime.*
import androidx.lifecycle.*

class SharedBetweenMedicineRepoAndNewRemindViewModel : ViewModel() {
    var medicineRepoId: Int? by mutableStateOf(null)

    fun changeMedicineRepoId(id: Int) {
        medicineRepoId = id
    }
}