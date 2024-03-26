package org.hinanawiyuzu.qixia.ui.state

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