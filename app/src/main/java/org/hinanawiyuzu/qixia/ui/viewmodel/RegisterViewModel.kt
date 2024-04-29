package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.lifecycle.*
import androidx.navigation.*
import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.ui.route.*

class RegisterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()
    fun onAccountPasswordChanged(value: String) {
        _uiState.update {
            it.copy(
                accountPassword = value,
                isPasswordError = !(value.matches(Regex("[a-zA-Z0-9.]+"))
                        && value.length in (6..18))
            )
        }
    }

    fun onAccountPhoneChanged(value: String) {
        _uiState.update {
            it.copy(
                accountPhone = value,
                // 电话号码显然应该是11位吧。反正不可能出海的。
                isPhoneError = !(value.length == 11 && value.matches(Regex("[0-9]+")))
            )
        }
    }

    fun onHidePasswordClicked() {
        _uiState.update {
            it.copy(hidePassword = !it.hidePassword)
        }
    }

    fun onNextButtonClicked(navController: NavController) {
        if (!_uiState.value.isPhoneError && !_uiState.value.isPasswordError
            && _uiState.value.accountPhone.isNotEmpty() && _uiState.value.accountPhone.isNotEmpty()
        ) {
            navController.navigate(
                route = "${LoginRoute.VerificationCodeScreen.name}/${
                    _uiState.value
                        .accountPhone
                }/${_uiState.value.accountPassword}"
            )
        } else {
            _uiState.update {
                it.copy(
                    isPhoneError = true,
                    isPasswordError = true
                )
            }
        }
    }

    fun onClauseClicked() {
        //TODO: 当产品条款被点击时，进入产品条款浏览界面
    }

    fun onPrivacyPolicyClicked() {
        //TODO: 当隐私政策被点击时，进入隐私政策浏览界面
    }
}

data class RegisterUiState(
    val accountPassword: String = "",
    val accountPhone: String = "",
    val hidePassword: Boolean = true,
    val isPhoneError: Boolean = false,
    val isPasswordError: Boolean = false
)