package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.hinanawiyuzu.qixia.ui.state.LoginUiState

class LoginViewModel : ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())

    // asStateFlow()会将可变状态流变为只读状态流
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    // 输入的账户名（电话号码）
    var accountPhone by mutableStateOf("")
        private set

    // 输入的账户密码
    var accountPassword by mutableStateOf("")
        private set

    // 是否隐藏密码
    var hidePassword by mutableStateOf(true)
        private set

    fun onAccountPhoneChanged(value: String) {
        accountPhone = value
    }

    fun onAccountPasswordChanged(value: String) {
        accountPassword = value
    }

    fun onHidePasswordClicked() {
        // 如果hidePassword是true,那么就变成false。如果是false，则变成true。
        // 其实就是布尔值取反
        hidePassword = hidePassword == false
    }

    fun onWechatLoginClicked() {
        //TODO: 微信登录，应当要调用第三方API
    }

    fun onAlipayLoginClicked() {
        //TODO: 支付宝登录，应当要调用第三方API
    }
}