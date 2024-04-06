package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*

class ResetPasswordViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState = _uiState.asStateFlow()
    fun onNewPasswordChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                newPassword = value
            )
        }
    }

    fun onConfirmNewPasswordChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                confirmNewPassword = value
            )
        }
    }

    fun onHideNewPasswordClicked() {
        _uiState.update { currentState ->
            currentState.copy(
                hideNewPassword = currentState.hideNewPassword.not()
            )
        }
    }

    fun onHideConfirmNewPasswordClicked() {
        _uiState.update { currentState ->
            currentState.copy(
                hideConfirmNewPassword = currentState.hideConfirmNewPassword.not()
            )
        }
    }
}

data class ResetPasswordUiState(
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val hideNewPassword: Boolean = true,
    val hideConfirmNewPassword: Boolean = true
)