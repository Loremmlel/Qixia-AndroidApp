package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import org.hinanawiyuzu.qixia.components.PasswordInputField
import org.hinanawiyuzu.qixia.ui.route.LoginRoute
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.viewmodel.RegisterViewModel
import org.hinanawiyuzu.qixia.utils.advancedShadow


/**
 * 注册界面主函数
 * @param modifier 修饰符
 * @param viewModel 该界面的ViewModel。
 * @param navController 默认为rememberNavController，一般无需传参
 * @author HinanawiYuzu
 */
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegisterPicture(
            modifier = Modifier
                .weight(0.33f)
                .padding(20.dp)
        )
        RegisterArea(
            modifier = Modifier.weight(0.4f),
            accountPassword = uiState.accountPassword,
            accountPhone = uiState.accountPhone,
            hidePassword = uiState.hidePassword,
            isPhoneError = uiState.isPhoneError,
            isPasswordError = uiState.isPasswordError,
            onAccountPasswordChanged = viewModel::onAccountPasswordChanged,
            onAccountPhoneChanged = viewModel::onAccountPhoneChanged,
            onHidePasswordClicked = viewModel::onHidePasswordClicked,
            onNextButtonClicked = { viewModel.onNextButtonClicked(navController) },
            onClauseClicked = viewModel::onClauseClicked,
            onPrivacyPolicyClicked = viewModel::onPrivacyPolicyClicked
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
 * @param accountPassword 用户输入的密码，应当传入ViewModel中的属性
 * @param accountPhone 用户输入的电话号码，应当传入ViewModel中的属性
 * @param hidePassword 是否隐藏密码
 * @param isPasswordError 密码输入是否符合格式要求
 * @param isPhoneError 电话输入是否符合格式要求
 * @param onAccountPasswordChanged 用户输入账户名的处理事件，应当传入ViewModel中的方法
 * @param onAccountPhoneChanged 用户输入账户电话号码的处理事件，应当传入ViewModel中的方法
 * @param onNextButtonClicked 用户点击继续的处理事件。
 * @param onClauseClicked 用户点击条款和条件的处理事件，应当跳转到对应的页面。
 * @param onPrivacyPolicyClicked 用户点击隐私政策的处理事件，应当跳转到对应的页面。
 * @author HinanawiYuzu
 */
@Composable
private fun RegisterArea(
    modifier: Modifier = Modifier,
    accountPhone: String,
    accountPassword: String,
    hidePassword: Boolean,
    isPhoneError: Boolean,
    isPasswordError: Boolean,
    onAccountPhoneChanged: (String) -> Unit,
    onAccountPasswordChanged: (String) -> Unit,
    onHidePasswordClicked: () -> Unit,
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
        // 电话号码输入
        CommonInputField(
            modifier = Modifier.fillMaxWidth(),
            leadingIconRes = R.drawable.login_screen_call,
            placeholderTextRes = R.string.login_screen_account_name_input_placeholder,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            isError = isPhoneError,
            errorMessage = "请输入正确的电话号码!",
            value = accountPhone,
            onValueChanged = { onAccountPhoneChanged(it) },
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        PasswordInputField(
            modifier = Modifier.fillMaxWidth(),
            password = accountPassword,
            hidePassword = hidePassword,
            isError = isPasswordError,
            errorMessage = "密码少于18位,大于6位;且只能包括大小写字母、数字和.",
            onPasswordChanged = onAccountPasswordChanged,
            onHidePasswordClicked = onHidePasswordClicked,
            placeholderTextRes = R.string.login_screen_account_password_input_placeholder
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