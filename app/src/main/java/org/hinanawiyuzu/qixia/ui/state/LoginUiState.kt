package org.hinanawiyuzu.qixia.ui.state

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.hinanawiyuzu.qixia.R

data class LoginUiState(
    @DrawableRes val hidePasswordIconRes: Int = R.drawable.login_screen_hide_password,
    @StringRes val hidePasswordIconDescRes: Int = R.string.login_screen_hide_password
)