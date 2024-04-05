package org.hinanawiyuzu.qixia.ui.viewmodel.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedBetweenMedicineRepoAndNewRemindViewModel: ViewModel() {
    var medicineRepoId: Int? by mutableStateOf(null)

    fun changeMedicineRepoId(id: Int) {
        medicineRepoId = id
    }
}