package org.hinanawiyuzu.qixia.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.CommonButton
import org.hinanawiyuzu.qixia.components.PasswordInputField
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.viewmodel.ResetPasswordViewModel
import org.hinanawiyuzu.qixia.utils.LoginRoute
import org.hinanawiyuzu.qixia.utils.advancedShadow

@Composable
fun ResetPasswordScreen(
    modifier: Modifier = Modifier,
    resetPasswordViewModel: ResetPasswordViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    val resetPasswordUiState by resetPasswordViewModel.uiState.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Picture(
            modifier = modifier
                .weight(0.33f)
                .padding(20.dp)
        )
        InputArea(
            modifier = modifier.weight(0.4f),
            newPassword = resetPasswordUiState.newPassword,
            confirmNewPassword = resetPasswordUiState.confirmNewPassword,
            hideNewPassword = resetPasswordUiState.hideNewPassword,
            hideConfirmNewPassword = resetPasswordUiState.hideConfirmNewPassword,
            onNewPasswordChanged = resetPasswordViewModel::onNewPasswordChanged,
            onConfirmNewPasswordChanged = resetPasswordViewModel::onConfirmNewPasswordChanged,
            onHideNewPasswordClicked = resetPasswordViewModel::onHideNewPasswordClicked,
            onHideConfirmNewPasswordClicked =
            resetPasswordViewModel::onHideConfirmNewPasswordClicked
        )
        SubmitButton(
            modifier = modifier
                .fillMaxWidth(0.8f)
                .weight(0.3f),
            onSubmitButtonClicked = {
                navController.navigate(route = LoginRoute.LoginScreen.name) {
                    popUpTo(LoginRoute.LoginScreen.name) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
    }
}

@Stable
@Composable
private fun Picture(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.reset_password_screen_picture),
            contentDescription = null
        )
    }
}

@Composable
private fun InputArea(
    modifier: Modifier = Modifier,
    newPassword: String,
    confirmNewPassword: String,
    hideNewPassword: Boolean,
    hideConfirmNewPassword: Boolean,
    onNewPasswordChanged: (String) -> Unit,
    onConfirmNewPasswordChanged: (String) -> Unit,
    onHideNewPasswordClicked: () -> Unit,
    onHideConfirmNewPasswordClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = stringResource(R.string.reset_password_screen_reset_password),
            style = TextStyle(
                fontSize = FontSize.extraLargeSize
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        // 新密码
        PasswordInputField(
            modifier = Modifier.fillMaxWidth(),
            password = newPassword,
            hidePassword = hideNewPassword,
            onPasswordChanged = onNewPasswordChanged,
            onHidePasswordClicked = onHideNewPasswordClicked,
            placeholderTextRes = R.string.reset_password_screen_input_new_password_placeholder,
            imeAction = ImeAction.Next
        )
        // 确认密码
        PasswordInputField(
            modifier = Modifier.fillMaxWidth(),
            password = confirmNewPassword,
            hidePassword = hideConfirmNewPassword,
            onPasswordChanged = onConfirmNewPasswordChanged,
            onHidePasswordClicked = onHideConfirmNewPasswordClicked,
            placeholderTextRes = R.string.reset_password_screen_input_confirm_new_password_placeholder
        )
    }
}

@Composable
private fun SubmitButton(
    modifier: Modifier = Modifier,
    onSubmitButtonClicked: () -> Unit
) {
    CommonButton(
        modifier = modifier
            .requiredHeight(dimensionResource(id = R.dimen.login_screen_login_button_height))
            .advancedShadow(alpha = 0.4f, shadowBlurRadius = 5.dp, offsetY = 5.dp),
        buttonTextRes = R.string.reset_password_screen_submit_button_text,
        onButtonClicked = onSubmitButtonClicked
    )
}

@Preview
@Composable
fun ResetPasswordScreenPreview() {
    QixiaTheme {
        ResetPasswordScreen()
    }
}