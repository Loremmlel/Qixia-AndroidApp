package org.hinanawiyuzu.qixia.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.hinanawiyuzu.qixia.QixiaApplication
import org.hinanawiyuzu.qixia.data.entity.MedicineRepo
import org.hinanawiyuzu.qixia.data.repo.MedicineRepoRepository
import org.hinanawiyuzu.qixia.utils.showShortToast

// 本来想着能不能抽象出一个抽象类来减少重复代码的，但是因为viewModel的构建是工厂模式，所以也不敢动.
class MedicineRepoListViewModel(
  private val medicineRepoRepository: MedicineRepoRepository,
  private val application: QixiaApplication
) : ViewModel() {
  // 用户选择的排序方式
  private var sortCondition: SortCondition? by mutableStateOf(null)

  // 用户输入的搜索内容
  var userSearchInput: String? by mutableStateOf(null)
    private set

  /**
   * 显示的药品信息。由allMedicineRepo经过排序、筛选方式得到
   */
  var displayedMedicineRepo: List<MedicineRepo> by mutableStateOf(emptyList())
    private set

  lateinit var selectedStates: MutableList<Boolean>
    private set
  val allMedicineRepo: StateFlow<AllMedicineRepo> = medicineRepoRepository.getAllStream()
    .map { allMedicineRepo ->
      AllMedicineRepo(allMedicineRepo.filter { it.userId == application.currentLoginUserId!! })
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = AllMedicineRepo()
    )

  var deleteButtonEnabled: Boolean by mutableStateOf(false)

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
      SortCondition.ByNameDESC -> displayedMedicineRepo.sortedByDescending { it.name }
      SortCondition.ByNameASC -> displayedMedicineRepo.sortedBy { it.name }
      SortCondition.ByExpiryDateDESC -> displayedMedicineRepo.sortedByDescending { it.expiryDate }
      SortCondition.ByExpiryDateASC -> displayedMedicineRepo.sortedBy { it.expiryDate }
      SortCondition.ByRemainAmountDESC -> displayedMedicineRepo.sortedByDescending { it.remainAmount }
      SortCondition.ByRemainAmountASC -> displayedMedicineRepo.sortedBy { it.remainAmount }
      else -> displayedMedicineRepo
    }
  }

  // 删除的话，可以多选吧。
  fun toggleSelection(index: Int) {
    selectedStates[index] = !selectedStates[index]
    deleteButtonEnabled = selectedStates.any { it }
  }

  fun onUserSearchInputChanged(input: String) {
    userSearchInput = input
  }

  fun startSearch() {
    displayedMedicineRepo = allMedicineRepo.value.allMedicineRepoList.filter {
      it.name.contains(userSearchInput ?: "", ignoreCase = true)
    }
  }

  fun onDeleteClicked(context: Context) {
    val selectedMedicineRepo = allMedicineRepo.value.allMedicineRepoList.filterIndexed { index, _ ->
      selectedStates[index]
    }
    viewModelScope.launch {
      selectedMedicineRepo.forEach {
        medicineRepoRepository.delete(it)
      }
      showShortToast(context, "删除成功")
    }
  }

}