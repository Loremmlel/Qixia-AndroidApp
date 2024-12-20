package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hinanawiyuzu.qixia.data.repo.UserRepository
import org.hinanawiyuzu.qixia.ui.route.AppRoute
import org.hinanawiyuzu.qixia.ui.route.LoginRoute

class LoginViewModel(
  private val userRepository: UserRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(LoginUiState())
  val uiState = _uiState.asStateFlow()

  fun onAccountPhoneChanged(value: String) {
    _uiState.update { currentState ->
      currentState.copy(accountPhone = value)
    }
  }

  fun onAccountPasswordChanged(value: String) {
    _uiState.update { currentState ->
      currentState.copy(accountPassword = value)
    }
  }

  fun onHidePasswordClicked() {
    _uiState.update { currentState ->
      currentState.copy(hidePassword = currentState.hidePassword.not())
    }
  }

  fun onLoginButtonClicked(navController: NavController) {
    viewModelScope.launch {
      val queryResult =
        userRepository.getStreamByPhone(_uiState.value.accountPhone)
          .firstOrNull()
      if (queryResult == null) {
        _uiState.update {
          it.copy(isError = true)
        }
      } else {
        if (queryResult.password != _uiState.value.accountPassword || queryResult.loginState) {
          _uiState.update {
            it.copy(isError = true)
          }
        } else {
          userRepository.update(queryResult.copy(loginState = true))
          navController.navigate(route = AppRoute.AppScreen.name) {
            navController.popBackStack(
              route = LoginRoute.LoginScreen.name,
              inclusive = true
            )
          }
        }
      }
      return@launch
    }
  }

  fun onWechatLoginClicked() {
    //TODO: 微信登录，应当要调用第三方API
  }

  fun onAlipayLoginClicked() {
    //TODO: 支付宝登录，应当要调用第三方API
  }
}

/**
 * @param accountPhone 账户电话号码
 * @param accountPassword 账户密码
 * @param hidePassword 是否隐藏密码
 */
data class LoginUiState(
  val accountPhone: String = "",
  val accountPassword: String = "",
  val hidePassword: Boolean = true,
  val isError: Boolean = false,
)