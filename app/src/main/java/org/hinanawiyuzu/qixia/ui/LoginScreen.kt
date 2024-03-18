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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.data.FontSize
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.theme.light_background
import org.hinanawiyuzu.qixia.ui.theme.secondary_color
import org.hinanawiyuzu.qixia.ui.viewmodel.LoginViewModel
import org.hinanawiyuzu.qixia.utils.advancedShadow

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // 登录界面状态
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    NavHost(navController = navController, startDestination = "LoginScreen") {
        composable(route = "LoginScreen") {
            Column(
            ) {
                LoginPicture(modifier.align(Alignment.CenterHorizontally))
                LoginArea(
                    accountName = loginViewModel.accountName,
                    accountPassword = loginViewModel.accountPassword,
                    hidePassword = loginViewModel.hidePassword,
                    onAccountNameChanged = { loginViewModel.onAccountNameChanged(it) },
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
                    Register(onRegisterClicked = { navController.navigate("RegisterScreen") })
                }
            }
        }
        composable(route = "ForgetPasswordScreen") {

        }
        composable(route = "RegisterScreen") {

        }
        composable(route = "MainScreen") {

        }
    }
}

@Stable
@Composable
fun LoginPicture(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginArea(
    modifier: Modifier = Modifier,
    accountName: String,
    accountPassword: String,
    hidePassword: Boolean,
    onAccountNameChanged: (String) -> Unit,
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
        // 账户名输入
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.login_screen_call),
                    contentDescription = stringResource(R.string.login_screen_account_name_input_desc)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(
                        R.string
                            .login_screen_account_name_input_placeholder
                    ),
                    style = TextStyle(color = Color.Gray)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone
            ),
            value = accountName,
            onValueChange = onAccountNameChanged
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        // 账户密码输入
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(R.string.login_screen_account_password_input_placeholder),
                    style = TextStyle(color = Color.Gray)
                )
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.login_screen_password),
                    contentDescription = stringResource(R.string.login_screen_account_password_input_desc)
                )
            },
            trailingIcon = {
                IconButton(onClick = onHidePasswordClicked) {
                    Icon(
                        painter = painterResource(
                            id = if (hidePassword) R.drawable.login_screen_hide_password
                            else R.drawable.login_screen_password //TODO 显示密码的XML还没有
                        ),
                        contentDescription = stringResource(
                            id = if (hidePassword) R.string.login_screen_hide_password
                            else R.string.login_screen_display_password
                        )
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White
            ),
            value = accountPassword,
            onValueChange = onAccountPasswordChanged
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
        Button(
            onClick = onLoginButtonClicked,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(dimensionResource(id = R.dimen.login_screen_login_button_height))
                .align(Alignment.CenterHorizontally)
                .advancedShadow(alpha = 0.4f, shadowBlurRadius = 5.dp, offsetY = 5.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = secondary_color
            )
        ) {
            Text(
                text = stringResource(R.string.login_screen_button_text),
                style = TextStyle(fontSize = FontSize.loginScreenLoginButtonTextSize)
            )
        }
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


@Composable
fun ThirdPartyLogin(
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

@Composable
fun Register(
    modifier: Modifier = Modifier,
    onRegisterClicked: () -> Unit
) {
    Row(
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

@Preview()
@Composable
fun LoginScreenPreview() {
    QixiaTheme {
        LoginScreen()
    }
}