package org.hinanawiyuzu.qixia.ui.state

data class ResetPasswordUiState(
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val hideNewPassword: Boolean = true,
    val hideConfirmNewPassword: Boolean = true
)