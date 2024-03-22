package org.hinanawiyuzu.qixia.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.CommonButton
import org.hinanawiyuzu.qixia.components.CommonInputField
import org.hinanawiyuzu.qixia.components.PasswordInputField
import org.hinanawiyuzu.qixia.data.FontSize
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.theme.light_background
import org.hinanawiyuzu.qixia.ui.viewmodel.LoginViewModel
import org.hinanawiyuzu.qixia.utils.advancedShadow

/**
 * 登录界面主函数。
 * @param modifier 修饰符
 * @param loginViewModel 传入LoginViewModel类。默认值为初始化的LoginViewModel
 * @param navController 导航控制，默认为rememberNavController类型。一般无需传参
 * @author HinanawiYuzu
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "LoginScreen") {
        composable(route = "LoginScreen") {
            Column {
                LoginPicture(
                    modifier
                        .align(Alignment.CenterHorizontally)
                        .weight(0.33f)
                )
                LoginArea(
                    modifier = Modifier.weight(0.4f),
                    accountPhone = loginViewModel.accountPhone,
                    accountPassword = loginViewModel.accountPassword,
                    hidePassword = loginViewModel.hidePassword,
                    onAccountPhoneChanged = { loginViewModel.onAccountPhoneChanged(it) },
                    onAccountPasswordChanged = { loginViewModel.onAccountPasswordChanged(it) },
                    onHidePasswordClicked = { loginViewModel.onHidePasswordClicked() },
                    onForgetPasswordClicked = { navController.navigate("ForgetPasswordScreen") },
                    // TODO:应该加登录验证的逻辑。因为可能涉及到数据层，所以暂时不写。
                    onLoginButtonClicked = { navController.navigate("MainScreen") },
                )
                ThirdPartyLogin(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onWechatLoginClicked = { loginViewModel.onWechatLoginClicked() },
                    onAlipayLoginClicked = { loginViewModel.onAlipayLoginClicked() }
                )
                Column(
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    RegisterArea(onRegisterClicked = { navController.navigate("RegisterScreen") })
                }
            }
        }
        composable(route = "ForgetPasswordScreen") {

        }
        composable(route = "RegisterScreen") {
            RegisterScreen()
        }
        composable(route = "MainScreen") {

        }
    }
}

/**
 * 登录界面的图片
 * @param modifier 修饰符
 * @author HinanawiYuzu
 */
@Stable
@Composable
private fun LoginPicture(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_screen_picture),
            contentDescription = null
        )
    }
}


/**
 * 用户输入框和忘记密码、登录按钮区域。
 * @param modifier 修饰符
 * @param accountPhone 用户输入的电话号，应当传入ViewModel的属性。
 * @param accountPassword 用户输入的密码，应当传入ViewModel的属性。
 * @param hidePassword 是否隐藏密码，应当传入ViewModel的属性。
 * @param onAccountPhoneChanged 用户输入电话号的处理事件，应当传入ViewModel的方法。
 * @param onAccountPasswordChanged 用户输入密码的处理事件，应当传入ViewModel的方法。
 * @param onHidePasswordClicked 用户点击是否隐藏密码的处理事件，应当传入ViewModel的方法。
 * @param onForgetPasswordClicked 用户点击忘记密码的处理事件。应当导航到恢复密码界面。
 * @param onLoginButtonClicked 用户点击登录的处理事件，应当传入ViewModel的方法。
 * @author HinanawiYuzu
 */
@Composable
private fun LoginArea(
    modifier: Modifier = Modifier,
    accountPhone: String,
    accountPassword: String,
    hidePassword: Boolean,
    onAccountPhoneChanged: (String) -> Unit,
    onAccountPasswordChanged: (String) -> Unit,
    onHidePasswordClicked: () -> Unit,
    onForgetPasswordClicked: () -> Unit,
    onLoginButtonClicked: () -> Unit
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
            text = stringResource(R.string.login_screen_login),
            style = TextStyle(
                fontSize = FontSize.loginScreenLoginSize
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        // 账户电话输入
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
        // 账户密码输入
        PasswordInputField(
            modifier = Modifier.fillMaxWidth(),
            password = accountPassword,
            hidePassword = hidePassword,
            onPasswordChanged = onAccountPasswordChanged,
            onHidePasswordClicked = onHidePasswordClicked,
            placeholderTextRes = R.string.login_screen_account_password_input_placeholder
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        TextButton(
            onClick = onForgetPasswordClicked,
            modifier = Modifier
                .align(Alignment.End)
        ) {
            Text(
                text = stringResource(R.string.login_screen_forget_password),
                style = TextStyle(fontSize = FontSize.loginScreenForgetPasswordSize)
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        CommonButton(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(dimensionResource(id = R.dimen.login_screen_login_button_height))
                .align(Alignment.CenterHorizontally)
                .advancedShadow(alpha = 0.4f, shadowBlurRadius = 5.dp, offsetY = 5.dp),
            buttonTextRes = R.string.login_screen_button_text,
            onButtonClicked = onLoginButtonClicked
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.transverse_line),
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.login_screen_or),
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = FontSize.loginScreenOrSize
                )
            )
            Image(
                painter = painterResource(id = R.drawable.transverse_line),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}


/**
 * 第三方登录区域。
 * @param modifier 修饰符
 * @param onAlipayLoginClicked 支付宝登录按钮的点击事件，应当传入ViewModel的方法
 * @param onWechatLoginClicked 微信登录按钮的点击事件，应当传入ViewModel的方法
 * @author HinanawiYuzu
 */
@Composable
private fun ThirdPartyLogin(
    modifier: Modifier = Modifier,
    onAlipayLoginClicked: () -> Unit,
    onWechatLoginClicked: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .padding(start = 15.dp, end = 15.dp)
            .height(dimensionResource(id = R.dimen.login_screen_login_button_height))
            .background(light_background),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                IconButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    onClick = onWechatLoginClicked
                ) {
                    Icon(
                        tint = Color.Unspecified,
                        painter = painterResource(id = R.drawable.wechat_icon),
                        contentDescription = stringResource(R.string.login_screen_wechat_login)
                    )
                }
                IconButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    onClick = onAlipayLoginClicked
                ) {
                    Icon(
                        tint = Color.Unspecified,
                        painter = painterResource(id = R.drawable.alipay_icon),
                        contentDescription = stringResource(R.string.login_screen_alipay_login)
                    )
                }
            }
            Text(
                text = stringResource(R.string.login_screen_third_party_login),
                style = TextStyle(
                    fontSize = FontSize.loginScreenThirdPartySize,
                    color = Color.DarkGray
                )
            )
        }
    }
}

/**
 * 提示未注册用户注册的区域。
 * @param modifier 修饰符
 * @param onRegisterClicked “注册”文字点击事件，应当跳转到注册界面。
 * @author HinanawiYuzu
 */
@Composable
private fun RegisterArea(
    modifier: Modifier = Modifier,
    onRegisterClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.login_screen_no_account)
        )
        TextButton(onClick = onRegisterClicked) {
            Text(
                text = stringResource(R.string.login_screen_register_new_account),
                style = TextStyle(fontSize = FontSize.normalSize)
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    QixiaTheme {
        LoginScreen()
    }
}