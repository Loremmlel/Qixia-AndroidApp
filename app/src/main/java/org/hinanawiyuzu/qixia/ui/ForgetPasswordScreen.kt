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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.CommonButton
import org.hinanawiyuzu.qixia.components.CommonInputField
import org.hinanawiyuzu.qixia.data.FontSize
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.viewmodel.ForgetPasswordViewModel
import org.hinanawiyuzu.qixia.utils.advancedShadow

@Composable
fun ForgetPasswordScreen(
    modifier: Modifier = Modifier,
    forgetPasswordViewModel: ForgetPasswordViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ForgetPasswordPicture(
            modifier = modifier.weight(0.33f)
        )
        ForgetPasswordPhoneInputArea(
            modifier = modifier.weight(0.4f),
            accountPhone = forgetPasswordViewModel.accountPhone,
            onAccountPhoneChanged = { forgetPasswordViewModel.onAccountPhoneChanged(it) }
        )
        ForgetPasswordSubmitButton(
            modifier = modifier
                .weight(0.4f)
                .fillMaxWidth(0.8f),
            onSubmitButtonClicked = {}
        )
    }
}

@Stable
@Composable
fun ForgetPasswordPicture(
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
fun ForgetPasswordPhoneInputArea(
    modifier: Modifier = Modifier,
    accountPhone: String,
    onAccountPhoneChanged: (String) -> Unit
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
            text = "忘记密码?",
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
fun ForgetPasswordSubmitButton(
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
fun ForgetPasswordScreenPreview() {
    QixiaTheme {
        ForgetPasswordScreen()
    }
}