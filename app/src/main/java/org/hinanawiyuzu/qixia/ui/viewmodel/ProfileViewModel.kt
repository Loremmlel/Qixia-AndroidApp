package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.hinanawiyuzu.qixia.QixiaApplication
import org.hinanawiyuzu.qixia.data.repo.UserRepository

class ProfileViewModel(
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
  var isVIPVisible by mutableStateOf(true)
  fun closeVIP() {
    isVIPVisible = false
  }

  fun onLogoutClicked() {
  }

  fun onNotificationClicked(navController: NavHostController) {
  }

  fun onCustomerServiceClicked(navController: NavHostController) {
  }

  fun onRecordExportClicked(navController: NavHostController) {
  }

  fun onSettingClicked(navController: NavHostController) {
  }

  fun onAboutUsClicked(navController: NavHostController) {
  }

  fun onHelpAndFeedbackClicked(navController: NavHostController) {
  }
}