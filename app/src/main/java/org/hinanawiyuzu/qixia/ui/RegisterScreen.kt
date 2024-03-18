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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.data.FontSize
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.theme.secondary_color
import org.hinanawiyuzu.qixia.ui.viewmodel.RegisterViewModel
import org.hinanawiyuzu.qixia.utils.advancedShadow


@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegisterPicture(modifier = Modifier.weight(0.33f))
        RegisterArea(
            modifier = Modifier.weight(0.4f),
            accountName = registerViewModel.accountName,
            accountPhone = registerViewModel.accountPhone,
            onAccountNameChanged = { registerViewModel.onAccountNameChanged(it) },
            onAccountPhoneChanged = { registerViewModel.onAccountPhoneChanged(it) },
            onNextButtonClicked = {},
            onClauseClicked = { registerViewModel.onClauseClicked() },
            onPrivacyPolicyClicked = { registerViewModel.onPrivacyPolicyClicked()}
        )
        ReturnToLogin()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterArea(
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
                fontSize = FontSize.loginScreenLoginSize
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        // 姓名输入
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.register_screen_name),
                    contentDescription = stringResource(R.string.register_screen_name_input_desc)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.register_screen_name_input_placeholder),
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
        // 电话号码输入
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(R.string.register_screen_phone_input_placeholder),
                    style = TextStyle(color = Color.Gray)
                )
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.login_screen_call), //有点后悔加前缀了……
                    contentDescription = stringResource(R.string.register_screen_phone_input_desc)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White
            ),
            value = accountPhone,
            onValueChange = onAccountPhoneChanged
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_screen_spacer_size)))
        Button(
            onClick = onNextButtonClicked,
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
                text = stringResource(R.string.register_screen_button_text),
                style = TextStyle(fontSize = FontSize.loginScreenLoginButtonTextSize)
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        ClauseStatement(
            onClauseClicked = onClauseClicked,
            onPrivacyPolicyClicked = onPrivacyPolicyClicked
        )
    }
}

@Stable
@Composable
fun RegisterPicture(
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
 */
@Composable
fun ClauseStatement(
    modifier: Modifier = Modifier,
    onClauseClicked: () -> Unit,
    onPrivacyPolicyClicked: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(0.8f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
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
        horizontalArrangement = Arrangement.Start
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

@Composable
fun ReturnToLogin(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "加入过我们吗？")
        TextButton(onClick = { /*TODO*/ }) {
            Text(
                text = "登录",
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