package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
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
fun ForgetPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ForgetPasswordViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Picture(
            modifier = modifier.weight(0.33f)
        )
        PhoneInputArea(
            modifier = modifier.weight(0.2f),
            accountPhone = uiState.accountPhone,
            onAccountPhoneChanged = { viewModel.onAccountPhoneChanged(it) }
        )
        SubmitButton(
            modifier = modifier
                .weight(0.4f)
                .fillMaxWidth(0.8f),
            onSubmitButtonClicked = {
                //TODO: 检查账户是否存在的逻辑
                navController.navigate(route = LoginRoute.ResetPasswordScreen.name)
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
            painter = painterResource(id = R.drawable.forget_password_screen_picture),
            contentDescription = null
        )
    }
}

@Composable
private fun PhoneInputArea(
    modifier: Modifier = Modifier,
    accountPhone: String,
    onAccountPhoneChanged: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = stringResource(R.string.forget_password_screen_isForget),
            style = TextStyle(
                fontSize = FontSize.loginScreenLoginSize
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        // 账户电话输入
        CommonInputField(
            modifier = Modifier.fillMaxWidth(),
            value = accountPhone,
            leadingIconRes = R.drawable.login_screen_call,
            placeholderTextRes = R.string.login_screen_account_name_input_placeholder,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            onValueChanged = onAccountPhoneChanged
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
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
        buttonTextRes = R.string.forget_password_screen_submit_button_text,
        onButtonClicked = onSubmitButtonClicked
    )
}

@Preview
@Composable
private fun ForgetPasswordScreenPreview() {
    QixiaTheme {
        ForgetPasswordScreen()
    }
}