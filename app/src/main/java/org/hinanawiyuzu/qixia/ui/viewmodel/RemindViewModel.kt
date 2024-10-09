package org.hinanawiyuzu.qixia.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.hinanawiyuzu.qixia.QixiaApplication
import org.hinanawiyuzu.qixia.data.entity.MedicineRemind
import org.hinanawiyuzu.qixia.data.repo.MedicineRemindRepository
import org.hinanawiyuzu.qixia.data.repo.MedicineRepoRepository
import org.hinanawiyuzu.qixia.utils.numberOfMedicineTakenUntilSpecificDate
import org.hinanawiyuzu.qixia.utils.showLongToast
import org.hinanawiyuzu.qixia.utils.showShortToast
import java.time.LocalDate
import java.time.LocalDateTime

class RemindViewModel(
  private val medicineRemindRepository: MedicineRemindRepository,
  private val medicineRepoRepository: MedicineRepoRepository,
  private val application: QixiaApplication
) : ViewModel() {
  private val currentDate: LocalDate = LocalDate.now()

  // 用户当前选择的时间
  var currentSelectedDate: LocalDate by mutableStateOf(currentDate)
    private set

  // 一次性查出来所有的提醒信息。客户端这样做我觉得没啥问题，因为提醒信息不会很多
  val allMedicineRemind: StateFlow<AllMedicineRemind> =
    medicineRemindRepository.getAllStream()
      .map { allMedicineRemind ->
        AllMedicineRemind(allMedicineRemind.filter { it.userId == application.currentLoginUserId })
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AllMedicineRemind()
      )
  val allMedicineRepo: StateFlow<AllMedicineRepo> =
    medicineRepoRepository.getAllStream()
      .map { allMedicineRepo ->
        AllMedicineRepo(allMedicineRepo.filter { it.userId == application.currentLoginUserId })
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AllMedicineRepo()
      )

  /**
   * 用户点击了日历上的某一天, 用来更新currentSelectedDate
   * @param index 点击的日历的位置,因为显示的是前后30天，所以15表示当天。
   */
  fun onCalendarClicked(index: Int) {
    currentSelectedDate = LocalDate.now().plusDays((index - 15).toLong())
  }

  // 根据repoId来获取对应的Uri。
  // 其实本来是想一次性输入一个列表返回也是一个列表的，但是我怕顺序会被打乱，所以就写成了一个一个查
  // 嘛不过我觉得对于客户端这样是没啥关系的，毕竟数据量很少
  /**
   * 根据提醒信息中的对应仓库id来查找对应的药品图片
   * @param reminds 提醒信息
   * @return 药品图片的Uri
   */
  fun searchImageFromMedicineRepo(reminds: MedicineRemind): Uri {
    return allMedicineRepo.value.allMedicineRepoList.find { it.id == reminds.medicineRepoId }?.imageUri ?: Uri.EMPTY
  }

  /**
   * 用户点击了服药按钮后执行的函数。
   *
   * 1. 找到对应的提醒信息和仓库信息
   * 2. 更新提醒信息的服药状态
   * 3. 更新仓库信息的剩余数量
   * 4. 显示Toast
   * @param id 点击的提醒信息的id
   * @param context 用来显示Toast
   * @author HinanawiYuzu
   */
  fun onTakeMedicineClicked(id: Int, context: Context) {
    try {
      val remind = allMedicineRemind.value.medicineRemindList.find { it.id == id }
      val repo = allMedicineRepo.value.allMedicineRepoList.find { it.id == remind?.medicineRepoId }
      if (remind != null && repo != null) {
        viewModelScope.launch(Dispatchers.IO) {
          val newRemind = remind.copy(
            isTaken = remind.isTaken.mapIndexed { index, b ->
              if (index == remind.startDate.numberOfMedicineTakenUntilSpecificDate(
                  remind.frequency, LocalDate.now()
                ) - 1
              ) true else b
            },
            takeTime = remind.takeTime.mapIndexed { index, nullableLocalDateTime ->
              if (index == remind.startDate.numberOfMedicineTakenUntilSpecificDate(
                  remind.frequency, LocalDate.now()
                ) - 1
              ) LocalDateTime.now() else nullableLocalDateTime
            }
          )
          val newRepo = repo.copy(
            remainAmount = (repo.remainAmount.toInt() - remind.dose.toInt()).toString()
          )
          medicineRemindRepository.update(newRemind)
          medicineRepoRepository.update(newRepo)
          // 用withContext来切换到主线程，因为Toast只能在主线程中执行。
          withContext(Dispatchers.Main) {
            showLongToast(context, "您已服用预定在${remind.remindTime}的${remind.name}")
          }
        }
      }
    } catch (e: Exception) {
      showShortToast(context, "出现错误")
    }
  }

}

data class AllMedicineRemind(
  val medicineRemindList: List<MedicineRemind> = emptyList()
)


