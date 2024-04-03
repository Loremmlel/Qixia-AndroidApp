package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import org.hinanawiyuzu.qixia.data.entity.MedicineRepo
import org.hinanawiyuzu.qixia.data.repo.MedicineRepoRepository
import org.hinanawiyuzu.qixia.data.source.fake.fakeMedicinesRepoInfo

class MedicineRepoViewModel(
    private val medicineRepoRepository: MedicineRepoRepository
): ViewModel() {
    var sortCondition: SortCondition? by mutableStateOf(null)
    var userSearchInput: String? by mutableStateOf(null)
    val fakeRepoInfo: List<MedicineRepo> = fakeMedicinesRepoInfo
    var selectedStates = List(fakeRepoInfo.size) { false }.toMutableStateList()

        fun onSortConditionChanged(index: Int) {
        sortCondition = SortCondition.entries[index]
    }

    fun onUserSearchInputChanged(input: String) {
        userSearchInput = input
    }

    fun startSearch() {

    }

    fun toggleSelection(index: Int) {
        val trueIndex: Int = selectedStates.indexOf(true)
        // 只能选中一个
        if (trueIndex != -1) {
            selectedStates[index] = !selectedStates[index]
            selectedStates[trueIndex] = false
        } else {
            selectedStates[index] = !selectedStates[index]
        }
    }
}

enum class SortCondition {
    ByNameDESC,
    ByNameASC,
    ByExpiryDateDESC,
    ByExpiryDateASC,
    ByRemainAmountDESC,
    ByRemainAmountASC,
    ByFrequencyDESC,
    ByFrequencyASC;
    fun convertToString(): String {
        return when(this) {
            ByNameDESC -> "按名称降序"
            ByNameASC -> "按名称升序"
            ByExpiryDateDESC -> "按到期时间降序"
            ByExpiryDateASC -> "按到期时间升序"
            ByRemainAmountDESC -> "按剩余数量降序"
            ByRemainAmountASC -> "按剩余数量升序"
            ByFrequencyDESC -> "按服药频率降序"
            ByFrequencyASC -> "按服药频率升序"
        }
    }
}

data class AllMedicineRepo(
    val allMedicineRepoList: List<MedicineRepo>
)

data class DisplayedMedicineRepo(
    val displayedMedicineRepoList: List<MedicineRepo>
)