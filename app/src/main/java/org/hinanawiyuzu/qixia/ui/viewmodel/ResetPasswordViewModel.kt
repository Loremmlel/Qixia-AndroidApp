package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ResetPasswordViewModel: ViewModel() {
    var newPassword by mutableStateOf("")
        private set
    var confirmNewPassword by mutableStateOf("")
        private set
    var hideNewPassword by mutableStateOf(true)
        private set
    var hideConfirmNewPassword by mutableStateOf(true)
        private set
    fun onNewPasswordChanged(value: String) {
        newPassword = value
    }
    fun onConfirmNewPasswordChanged(value: String) {
        confirmNewPassword = value
    }
    fun onHideNewPasswordClicked() {
        hideNewPassword = hideNewPassword == false
    }
    fun onHideConfirmNewPasswordClicked() {
        hideConfirmNewPassword = hideConfirmNewPassword == false
    }
}