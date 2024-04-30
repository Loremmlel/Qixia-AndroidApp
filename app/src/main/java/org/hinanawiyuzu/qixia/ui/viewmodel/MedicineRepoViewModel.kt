package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.hinanawiyuzu.qixia.QixiaApplication
import org.hinanawiyuzu.qixia.data.entity.MedicineRepo
import org.hinanawiyuzu.qixia.data.repo.MedicineRepoRepository
import org.hinanawiyuzu.qixia.ui.route.RemindRoute
import org.hinanawiyuzu.qixia.ui.viewmodel.SortCondition.ByExpiryDateASC
import org.hinanawiyuzu.qixia.ui.viewmodel.SortCondition.ByExpiryDateDESC
import org.hinanawiyuzu.qixia.ui.viewmodel.SortCondition.ByNameASC
import org.hinanawiyuzu.qixia.ui.viewmodel.SortCondition.ByNameDESC
import org.hinanawiyuzu.qixia.ui.viewmodel.SortCondition.ByRemainAmountASC
import org.hinanawiyuzu.qixia.ui.viewmodel.SortCondition.ByRemainAmountDESC

class MedicineRepoViewModel(
    medicineRepoRepository: MedicineRepoRepository,
    private val application: QixiaApplication
) : ViewModel() {
    // 用户选择的排序方式
    private var sortCondition: SortCondition? by mutableStateOf(null)

    // 用户输入的搜索内容
    var userSearchInput: String? by mutableStateOf(null)
        private set

    /**
     * 药品的选择情况
     */
    lateinit var selectedStates: MutableList<Boolean>
        private set

    /**
     * 显示的药品信息。由allMedicineRepo经过排序、筛选方式得到
     */
    var displayedMedicineRepo: List<MedicineRepo> by mutableStateOf(emptyList())
        private set

    /**
     * 查询到的所有药品信息
     */
    val allMedicineRepo: StateFlow<AllMedicineRepo> = medicineRepoRepository.getAllStream()
        .map { allMedicineRepo ->
            AllMedicineRepo(allMedicineRepo.filter { it.userId == application.currentLoginUserId!! })
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AllMedicineRepo()
        )

    init {
        viewModelScope.launch {
            allMedicineRepo.collect {
                selectedStates = MutableList(it.allMedicineRepoList.size) { false }.toMutableStateList()
                displayedMedicineRepo = it.allMedicineRepoList
            }
        }
    }

    fun onSortConditionChanged(index: Int) {
        sortCondition = SortCondition.entries[index]
        displayedMedicineRepo = when (sortCondition) {
            ByNameDESC -> displayedMedicineRepo.sortedByDescending { it.name }
            ByNameASC -> displayedMedicineRepo.sortedBy { it.name }
            ByExpiryDateDESC -> displayedMedicineRepo.sortedByDescending { it.expiryDate }
            ByExpiryDateASC -> displayedMedicineRepo.sortedBy { it.expiryDate }
            ByRemainAmountDESC -> displayedMedicineRepo.sortedByDescending { it.remainAmount }
            ByRemainAmountASC -> displayedMedicineRepo.sortedBy { it.remainAmount }
            else -> displayedMedicineRepo
        }
    }

    fun onUserSearchInputChanged(input: String) {
        userSearchInput = input
    }

    fun startSearch() {
        displayedMedicineRepo = allMedicineRepo.value.allMedicineRepoList.filter {
            it.name.contains(userSearchInput ?: "", ignoreCase = true)
        }
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

    fun onAddMedicineClicked(navController: NavHostController) {
        navController.navigate(RemindRoute.NewMedicineScreen.name)
    }

    fun onSelectClicked(
        navController: NavHostController,
        changeMedicineRepoId: (Int) -> Unit
    ) {
        if (selectedStates.indexOf(true) != -1) {
            val selectedMedicineRepo = allMedicineRepo.value.allMedicineRepoList[selectedStates.indexOf(true)]
            changeMedicineRepoId(selectedMedicineRepo.id)
            navController.popBackStack()
        }
    }
}

/**
 * 排序条件
 * @property ByNameDESC 按名称降序
 * @property ByNameASC 按名称升序
 * @property ByExpiryDateDESC 按到期时间降序
 * @property ByExpiryDateASC 按到期时间升序
 * @property ByRemainAmountDESC 按剩余数量降序
 * @property ByRemainAmountASC 按剩余数量升序
 */
enum class SortCondition {
    ByNameDESC,
    ByNameASC,
    ByExpiryDateDESC,
    ByExpiryDateASC,
    ByRemainAmountDESC,
    ByRemainAmountASC;

    fun convertToString(): String {
        return when (this) {
            ByNameDESC -> "按名称降序"
            ByNameASC -> "按名称升序"
            ByExpiryDateDESC -> "按到期时间降序"
            ByExpiryDateASC -> "按到期时间升序"
            ByRemainAmountDESC -> "按剩余数量降序"
            ByRemainAmountASC -> "按剩余数量升序"
        }
    }
}

data class AllMedicineRepo(
    val allMedicineRepoList: List<MedicineRepo> = emptyList()
)