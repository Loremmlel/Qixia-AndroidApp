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
import org.hinanawiyuzu.qixia.QixiaApplication
import org.hinanawiyuzu.qixia.data.repo.UserRepository

class VIPViewModel(
  userRepository: UserRepository,
  application: QixiaApplication
) : ViewModel() {
  val currentUser: StateFlow<CurrentLoginUser> = userRepository.getStreamById(application.currentLoginUserId!!)
    .map { CurrentLoginUser(listOf(it)) }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5_000L),
      initialValue = CurrentLoginUser()
    )
  var currentVIPVersion: VIPVersion by mutableStateOf(VIPVersion.Box)
  var currentVIPSetMeal: VIPSetMeal by mutableStateOf(VIPSetMeal.Lifelong)

  fun onVIPSetMealChanged(vipSetMeal: VIPSetMeal) {
    currentVIPSetMeal = vipSetMeal
  }
}

enum class VIPVersion {
  Box,
  Personal
}

enum class VIPSetMeal {
  Lifelong,
  Yearly,
  Quarterly,
  Monthly,
}