package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.*
import androidx.navigation.*
import androidx.navigation.compose.*
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.*
import org.hinanawiyuzu.qixia.ui.*
import org.hinanawiyuzu.qixia.ui.route.*
import org.hinanawiyuzu.qixia.ui.theme.*
import org.hinanawiyuzu.qixia.ui.viewmodel.*
import org.hinanawiyuzu.qixia.utils.advancedShadow
import org.hinanawiyuzu.qixia.utils.slideComposable

/**
 * 登录界面主函数。
 *
 * 同时兼管了登录、注册、验证码等页面的导航控制。
 * 因此可能会显得代码有些冗长。但是我目前还没想到优化的方法。
 * @param modifier 修饰符
 * @param viewModel 传入LoginViewModel类。默认值为初始化的LoginViewModel
 * @param navController 导航控制，默认为rememberNavController类型。一般无需传参
 * @author HinanawiYuzu
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.factory),
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()
    NavHost(navController = navController, startDestination = LoginRoute.LoginScreen.name) {
        composable(
            route = LoginRoute.LoginScreen.name,
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(500), targetOffsetX = { -it })
            },
            enterTransition = {
                slideInHorizontally(animationSpec = tween(500), initialOffsetX = { it })
            },
            // pop可以控制用户按返回键的动画，即NavigateUp,同Navigate区分开来。
            popEnterTransition = {
                slideInHorizontally(animationSpec = tween(500), initialOffsetX = { -it })
            }
        ) {
            Column {
                LoginPicture(
                    modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp)
                        .weight(0.33f)
                )
                LoginArea(
                    modifier = Modifier.weight(0.4f),
                    accountPhone = uiState.accountPhone,
                    accountPassword = uiState.accountPassword,
                    hidePassword = uiState.hidePassword,
                    onAccountPhoneChanged = viewModel::onAccountPhoneChanged,
                    onAccountPasswordChanged = viewModel::onAccountPasswordChanged,
                    onHidePasswordClicked = viewModel::onHidePasswordClicked,
                    onForgetPasswordClicked = {
                        navController.navigate(LoginRoute.ForgetPasswordScreen.name)
                    },
                    isError = uiState.isError,
                    // TODO:应该加登录验证的逻辑。因为可能涉及到数据层，所以暂时不写。
                    onLoginButtonClicked = { viewModel.onLoginButtonClicked(navController) },
                )
                ThirdPartyLogin(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onWechatLoginClicked = viewModel::onWechatLoginClicked,
                    onAlipayLoginClicked = viewModel::onAlipayLoginClicked
                )
                Column(
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    RegisterArea(
                        onRegisterClicked = { navController.navigate(LoginRoute.RegisterScreen.name) }
                    )
                }
            }
        }
        slideComposable(
            route = LoginRoute.RegisterScreen.name,
        ) {
            // 如果要传导航控制器，那么在被传的组件中，不能出现新的NavHost。否则会报异常。
            RegisterScreen(navController = navController)
        }
        slideComposable(
            route = "${LoginRoute.VerificationCodeScreen.name}/{accountPhone}/{accountPassword}",
            arguments = listOf(
                navArgument("accountPhone") { type = NavType.StringType },
                navArgument("accountPassword") {
                    type = NavType.StringType
                    nullable = true
                }
            ),
        ) {
            VerificationCodeScreen(navController = navController, navBackStackEntry = it)
        }
        slideComposable(
            route = "${LoginRoute.FillPersonalInformationScreen.name}/{accountPhone}/{accountPassword}",
            arguments = listOf(
                navArgument("accountPhone") { type = NavType.StringType },
                navArgument("accountPassword") {
                    type = NavType.StringType
                    nullable = true
                }
            ),
        ) {
            FillPersonalInformationScreen(navController = navController, backStackEntry = it)
        }
        slideComposable(
            route = LoginRoute.ForgetPasswordScreen.name,
        ) {
            ForgetPasswordScreen(navController = navController)
        }
        slideComposable(
            route = LoginRoute.ResetPasswordScreen.name,
        ) {
            ResetPasswordScreen(navController = navController)
        }
        composable(
            route = AppRoute.AppScreen.name,
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(500), targetOffsetX = { -it })
            },
            enterTransition = {
                slideInHorizontally(animationSpec = tween(500), initialOffsetX = { it })
            }
        ) {
            AppScreen()
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
    isError: Boolean,
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
                fontSize = FontSize.extraLargeSize
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
            isError = isError,
            value = accountPhone,
            onValueChanged = { onAccountPhoneChanged(it) },
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        // 账户密码输入
        PasswordInputField(
            modifier = Modifier.fillMaxWidth(),
            password = accountPassword,
            hidePassword = hidePassword,
            isError = isError,
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
                modifier = Modifier.weight(0.5f),
                painter = painterResource(id = R.drawable.transverse_line),
                contentDescription = null
            )
            Text(
                modifier = Modifier.weight(0.2f),
                text = stringResource(R.string.login_screen_or),
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = FontSize.loginScreenOrSize
                ),
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier.weight(0.5f),
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