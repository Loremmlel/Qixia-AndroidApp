package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

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
    val hidePassword: Boolean = true
)