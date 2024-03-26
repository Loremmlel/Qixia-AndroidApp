package org.hinanawiyuzu.qixia.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.CommonButton
import org.hinanawiyuzu.qixia.components.CommonInputField
import org.hinanawiyuzu.qixia.data.FontSize
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.viewmodel.RegisterViewModel
import org.hinanawiyuzu.qixia.utils.LoginRoute
import org.hinanawiyuzu.qixia.utils.advancedShadow


/**
 * 注册界面主函数
 * @param modifier 修饰符
 * @param registerViewModel 该界面的ViewModel。
 * @param navController 默认为rememberNavController，一般无需传参
 * @author HinanawiYuzu
 */
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val registerUiState by registerViewModel.uiState.collectAsState()
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegisterPicture(modifier = Modifier
            .weight(0.33f)
            .padding(20.dp))
        RegisterArea(
            modifier = Modifier.weight(0.4f),
            accountName = registerUiState.accountName,
            accountPhone = registerUiState.accountPhone,
            onAccountNameChanged = { registerViewModel.onAccountNameChanged(it) },
            onAccountPhoneChanged = { registerViewModel.onAccountPhoneChanged(it) },
            onNextButtonClicked = {
                navController.navigate(
                    route = LoginRoute.VerificationCodeScreen.name
                )
            },
            onClauseClicked = { registerViewModel.onClauseClicked() },
            onPrivacyPolicyClicked = { registerViewModel.onPrivacyPolicyClicked() }
        )
        ReturnToLogin(
            onReturnToLoginClicked = {
                navController.navigate(LoginRoute.LoginScreen.name) {
                    navController.popBackStack(LoginRoute.LoginScreen.name, inclusive = true)
                }
            }
        )
    }
}


/**
 * 输入框和继续按钮、条款条件和隐私政策区域。
 * @param modifier 修饰符
 * @param accountName 用户输入的账户名，应当传入ViewModel中的属性
 * @param accountPhone 用户输入的电话号码，应当传入ViewModel中的属性
 * @param onAccountNameChanged 用户输入账户名的处理事件，应当传入ViewModel中的方法
 * @param onAccountPhoneChanged 用户输入账户电话号码的处理事件，应当传入ViewModel中的方法
 * @param onNextButtonClicked 用户点击继续的处理事件。
 * @param onClauseClicked 用户点击条款和条件的处理事件，应当跳转到对应的页面。
 * @param onPrivacyPolicyClicked 用户点击隐私政策的处理事件，应当跳转到对应的页面。
 * @author HinanawiYuzu
 */
@Composable
private fun RegisterArea(
    modifier: Modifier = Modifier,
    accountName: String,
    accountPhone: String,
    onAccountNameChanged: (String) -> Unit,
    onAccountPhoneChanged: (String) -> Unit,
    onNextButtonClicked: () -> Unit,
    onClauseClicked: () -> Unit,
    onPrivacyPolicyClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 15.dp, end = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = stringResource(R.string.register_screen_register),
            style = TextStyle(
                fontSize = FontSize.extraLargeSize
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        // 姓名输入
        CommonInputField(
            modifier = Modifier.fillMaxWidth(),
            leadingIconRes = R.drawable.register_screen_name,
            placeholderTextRes = R.string.register_screen_name_input_placeholder,
            value = accountName,
            onValueChanged = onAccountNameChanged
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        // 电话号码输入
        CommonInputField(
            modifier = Modifier.fillMaxWidth(),
            leadingIconRes = R.drawable.login_screen_call,
            placeholderTextRes = R.string.login_screen_account_name_input_placeholder,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            value = accountPhone,
            onValueChanged = { onAccountPhoneChanged(it) },
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        CommonButton(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(dimensionResource(id = R.dimen.login_screen_login_button_height))
                .align(Alignment.CenterHorizontally)
                .advancedShadow(alpha = 0.4f, shadowBlurRadius = 5.dp, offsetY = 5.dp),
            buttonTextRes = R.string.register_screen_button_text,
            onButtonClicked = onNextButtonClicked
        )
        Spacer(modifier = Modifier.height(15.dp))
        ClauseStatement(
            onClauseClicked = onClauseClicked,
            onPrivacyPolicyClicked = onPrivacyPolicyClicked
        )
    }
}

/**
 * 注册界面的图片。
 * @param modifier 修饰符
 * @author HinanawiYuzu
 */
@Stable
@Composable
private fun RegisterPicture(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.register_screen_picture),
            contentDescription = null
        )
    }
}

/**
 * 条款和隐私政策声明部分
 * @param modifier 修饰符
 * @param onClauseClicked 条款点击处理事件
 * @param onPrivacyPolicyClicked 隐私政策点击处理事件
 * @author HinanawiYuzu
 */
@Composable
private fun ClauseStatement(
    modifier: Modifier = Modifier,
    onClauseClicked: () -> Unit,
    onPrivacyPolicyClicked: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(0.8f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.register_screen_agree))
        TextButton(
            onClick = onClauseClicked,
            contentPadding = PaddingValues(0.dp),
        ) {
            Text(
                maxLines = 1,
                text = stringResource(R.string.register_screen_clause),
                style = TextStyle(fontSize = FontSize.normalSize)
            )
        }
    }
    Row(
        modifier = modifier.fillMaxWidth(0.8f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.register_screen_and))
        TextButton(
            onClick = onPrivacyPolicyClicked,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                maxLines = 1,
                text = stringResource(R.string.register_screen_privacy_policy),
                style = TextStyle(fontSize = FontSize.normalSize)
            )
        }
    }
}

/**
 * 提示已注册用户登录的文字按钮区域
 * @param modifier 修饰符
 * @param onReturnToLoginClicked 返回登录界面按钮点击事件
 * @author HinanawiYuzu
 */
@Composable
private fun ReturnToLogin(
    modifier: Modifier = Modifier,
    onReturnToLoginClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.register_screen_already_join_us))
        TextButton(onClick = onReturnToLoginClicked) {
            Text(
                text = stringResource(R.string.register_screen_login),
                style = TextStyle(fontSize = FontSize.normalSize)
            )
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    QixiaTheme {
        RegisterScreen()
    }
}