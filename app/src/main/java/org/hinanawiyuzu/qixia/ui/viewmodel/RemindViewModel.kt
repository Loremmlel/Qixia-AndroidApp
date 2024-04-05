package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.hinanawiyuzu.qixia.data.entity.MedicineRemind
import org.hinanawiyuzu.qixia.data.repo.MedicineRemindRepository
import org.hinanawiyuzu.qixia.data.repo.MedicineRepoRepository
import java.time.LocalDateTime

class RemindViewModel(
    private val medicineRemindRepository: MedicineRemindRepository,
    private val medicineRepoRepository: MedicineRepoRepository
) : ViewModel() {
    //TODO: 除了以系统时间为基准外，也许需要联网获取数据.
    //TODO: 这个应该可以更改，然后根据日期显示昨天或者明天的预期提醒。
    var currentTime: LocalDateTime = LocalDateTime.now()
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

}

data class AllMedicineRemind(
    val medicineRemindList: List<MedicineRemind> = emptyList()
)


