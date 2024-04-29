package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.*
import androidx.navigation.*
import androidx.navigation.compose.*
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.*
import org.hinanawiyuzu.qixia.ui.route.*
import org.hinanawiyuzu.qixia.ui.theme.*
import org.hinanawiyuzu.qixia.ui.viewmodel.*
import org.hinanawiyuzu.qixia.utils.*

@Composable
fun ResetPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()
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
            newPassword = uiState.newPassword,
            confirmNewPassword = uiState.confirmNewPassword,
            hideNewPassword = uiState.hideNewPassword,
            hideConfirmNewPassword = uiState.hideConfirmNewPassword,
            onNewPasswordChanged = viewModel::onNewPasswordChanged,
            onConfirmNewPasswordChanged = viewModel::onConfirmNewPasswordChanged,
            onHideNewPasswordClicked = viewModel::onHideNewPasswordClicked,
            onHideConfirmNewPasswordClicked =
            viewModel::onHideConfirmNewPasswordClicked
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