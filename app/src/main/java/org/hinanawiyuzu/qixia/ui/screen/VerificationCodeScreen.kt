package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.CommonButton
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.theme.neutral_color
import org.hinanawiyuzu.qixia.ui.theme.verification_input_color
import org.hinanawiyuzu.qixia.ui.viewmodel.VerificationCodeViewModel
import org.hinanawiyuzu.qixia.utils.advancedShadow


@Composable
fun VerificationCodeScreen(
    modifier: Modifier = Modifier,
    viewModel: VerificationCodeViewModel = viewModel(),
    navController: NavController = rememberNavController(),
    navBackStackEntry: NavBackStackEntry
) {
    // 不能使用 val navBackStackEntry by navController.currentBackStackEntryAsState()
    // 会有内部错误产生，原因未知。还是得传参。
    val accountPassword: String? = navBackStackEntry.arguments?.getString("accountPassword")
    val accountPhone: String? = navBackStackEntry.arguments?.getString("accountPhone")
    val deviceWidth = LocalConfiguration.current.screenWidthDp
    viewModel.accountPhone = accountPhone!!
    viewModel.accountPassword = accountPassword
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopText(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 25.dp)
                .weight(0.2f)
        )
        InputArea(
            modifier = Modifier.weight(0.15f),
            verificationCodeLength = 4,
            focusRequesters = viewModel.focusRequesters,
            screenWidth = deviceWidth,
            verificationCodes = viewModel.verificationCodes,
            onValueChanged = viewModel::onTextFieldsInput,
            onBackSpaceClicked = viewModel::onBackspaceClicked
        )
        SendAgainText(
            modifier = Modifier
                .weight(0.1f)
                .align(Alignment.End)
                .padding(end = 20.dp),
            onSendAgainClicked = {
                //TODO:再次发送验证码
            }
        )
        ConfirmButton(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .weight(0.8f),
            onConfirmButtonClicked = { viewModel.onConfirmButtonClicked(navController) }
        )
    }
}

@Stable
@Composable
private fun TopText(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.verification_screen_please_input),
            style = TextStyle(
                fontSize = FontSize.verificationScreenLargeSize,
                fontWeight = FontWeight.SemiBold
            )
        )
        Text(
            text = stringResource(R.string.verification_screen_already_send),
            color = Color.DarkGray,
            style = TextStyle(fontSize = 18.sp)
        )
    }
}

/**
 * 用户验证码输入区域
 * @param modifier 修饰符
 * @param verificationCodeLength 验证码长度，本来想要设计为可变的，最后还是固定值4好了。但是保留了变量和参数。
 * @param focusRequesters 每个验证码输入框对应的焦点请求器列表。
 * @param screenWidth 屏幕宽度dp值。按理来说完全不需要当参数传递。
 * @param verificationCodes 显示的验证码列表。
 * @param onValueChanged 验证码改变的回调函数
 * @param onBackSpaceClicked 用户点击回退键的回调函数
 */
@Composable
private fun InputArea(
    modifier: Modifier = Modifier,
    verificationCodeLength: Int,
    focusRequesters: List<FocusRequester>,
    screenWidth: Int,
    verificationCodes: List<String>,
    onValueChanged: (String) -> Unit,
    onBackSpaceClicked: (Int) -> Unit
) {
    // Spacer的长度dp值。
    val spacerWidth = screenWidth.toDouble() / (verificationCodeLength * 6 + 1)
    Row(
        modifier = modifier
    ) {
        repeat(verificationCodeLength) {
            Spacer(modifier = Modifier.requiredWidth(spacerWidth.dp))
            VerificationInputField(
                lengthOfSide = (5 * spacerWidth).toInt(),
                id = it,
                focusRequester = focusRequesters[it],
                verificationCode = verificationCodes[it],
                onValueChanged = onValueChanged,
                onBackSpaceClicked = onBackSpaceClicked
            )
        }
        Spacer(modifier = Modifier.requiredWidth(spacerWidth.dp))
    }

}

/**
 * 一个验证码输入框。
 *
 * 应当传入适当的modifier来管理焦点
 * @param modifier 修饰符
 * @param id 自己给每个输入框分配的id。输入框在触发onValueChange函数时，会附带Id信息。
 * @param lengthOfSide 输入框边长。一般可以获取设备Dp宽度然后进行布局。为了实现正方形的效果呢。
 * @param focusRequester 焦点请求器。请给每个框配一个。
 * @param verificationCode 单个输入框的验证码
 * @param onValueChanged 常规的回调函数。没什么好说的
 * @param onBackSpaceClicked 当点击退格时，回到上一个输入框
 */
@Composable
private fun VerificationInputField(
    modifier: Modifier = Modifier,
    id: Int,
    lengthOfSide: Int,
    focusRequester: FocusRequester,
    verificationCode: String,
    onValueChanged: (String) -> Unit,
    onBackSpaceClicked: (Int) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    TextField(
        modifier = modifier
            .onKeyEvent {
                // 当用户按下退格键时，进行相应的处理。
                if (it.key == Key.Backspace) {
                    onBackSpaceClicked(id)
                    return@onKeyEvent true
                }
                false
            }
            .focusable()
            .focusRequester(focusRequester)
            .onFocusEvent {
                isFocused = it.isFocused
            }
            .requiredHeight(lengthOfSide.dp)
            .requiredWidth(lengthOfSide.dp)
            .then(
                if (isFocused) {
                    // 如果这个框正在被聚焦，那么给框添加边框。
                    Modifier.border(2.dp, neutral_color, RoundedCornerShape(percent = 20))
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(percent = 20),
        colors = TextFieldDefaults.colors(
            // 将输入框的下划线三种状态的颜色设置为透明，效果即隐藏下划线。
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedContainerColor = verification_input_color,
            focusedContainerColor = Color.White
        ),
        singleLine = true,
        textStyle = TextStyle(
            fontSize = (lengthOfSide / 2).sp,//输入的数字要和框的大小成比例……
            textAlign = TextAlign.Center, // 居中显示
            fontWeight = FontWeight.SemiBold,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        value = verificationCode,
        onValueChange = { onValueChanged("$it#$id") }
    )

}

@Composable
private fun SendAgainText(
    modifier: Modifier = Modifier,
    onSendAgainClicked: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = { onSendAgainClicked() }
    ) {
        Text(
            text = stringResource(R.string.verification_screen_send_again),
            style = TextStyle(fontSize = 20.sp)
        )
    }
}

@Composable
private fun ConfirmButton(
    modifier: Modifier = Modifier,
    onConfirmButtonClicked: () -> Unit
) {
    CommonButton(
        modifier = modifier
            .requiredHeight(dimensionResource(id = R.dimen.login_screen_login_button_height))
            .advancedShadow(alpha = 0.4f, shadowBlurRadius = 5.dp, offsetY = 5.dp),
        buttonTextRes = R.string.confirm_button_text,
        onButtonClicked = onConfirmButtonClicked
    )
}

@Preview
@Composable
fun VerificationCodeScreenPreview() {
    QixiaTheme {
        Column {

        }
    }
}