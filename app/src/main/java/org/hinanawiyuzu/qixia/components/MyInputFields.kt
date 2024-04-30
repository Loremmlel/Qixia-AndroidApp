package org.hinanawiyuzu.qixia.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.ui.theme.FontSize

/**
 * 通用组件之密码输入框
 * @param modifier 修饰符
 * @param password 要传递的密码字符串，一般是ViewModel中的属性
 * @param hidePassword 是否隐藏该输入框密码，一般是ViewModel中的属性
 * @param isError 输入框的错误标识。
 * @param errorMessage 显示在输入框下方的错误消息
 * @param onPasswordChanged 输入框的回调函数，一般是ViewModel中的方法
 * @param onHidePasswordClicked 隐藏密码的按钮，一般是ViewModel中的方法
 * @param placeholderTextRes 占位文本的资源ID值
 * @param imeAction 指定该输入框的键盘完成符是什么。一般有ImeAction.Done，ImeAction.Next
 * @author HinanawiYuzu
 */
@Composable
fun PasswordInputField(
    modifier: Modifier = Modifier,
    password: String,
    hidePassword: Boolean,
    isError: Boolean = false,
    errorMessage: String = "账户或密码输入错误",
    onPasswordChanged: (String) -> Unit,
    onHidePasswordClicked: () -> Unit,
    @StringRes placeholderTextRes: Int,
    imeAction: ImeAction = ImeAction.Done,
) {
    TextField(
        modifier = modifier,
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(id = placeholderTextRes),
                style = TextStyle(color = Color.Gray, fontSize = FontSize.normalSize)
            )
        },
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.login_screen_password),
                contentDescription = null
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
        supportingText = {
            if (isError) {
                Text(
                    text = errorMessage,
                    style = TextStyle(
                        color = Color.Red,
                        fontSize = FontSize.tinySize
                    )
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
        ),
        visualTransformation =
        if (hidePassword) PasswordVisualTransformation()
        else VisualTransformation.None,
        value = password,
        isError = isError,
        onValueChange = { onPasswordChanged(it) }
    )
}

/**
 * 普通输入框。支持自定义图标、提示文字，输入框颜色、键盘设置。
 *
 * 说实话，仅仅只是封装了icon和placeholder而已。
 * @param modifier 修饰符
 * @param value 传递的输入值，一般是ViewModel中的属性
 * @param leadingIconRes 图标的资源id
 * @param placeholderTextRes 提示文字的资源id
 * @param colors 输入框的颜色设置。请使用TextFieldDefaults.colors(..自定义颜色..)
 * @param isError 是否错误，建议传入ViewModel的数据。
 * @param errorMessage 显示在输入框下方的错误信息
 * @param keyboardOptions 输入框的键盘设置。请使用KeyboardOptions.Default.copy(..自定义设置..)
 * @param onValueChanged 输入框的回调函数，一般是ViewModel中的方法
 * @author HinanawiYuzu
 */
@Composable
fun CommonInputField(
    modifier: Modifier = Modifier,
    value: String,
    @DrawableRes leadingIconRes: Int,
    @StringRes placeholderTextRes: Int,
    colors: TextFieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color.White
    ),
    isError: Boolean = false,
    errorMessage: String = "账户或密码输入错误",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChanged: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        singleLine = true,
        leadingIcon = {
            Image(
                painter = painterResource(id = leadingIconRes),
                contentDescription = null
            )
        },
        placeholder = {
            Text(
                text = stringResource(id = placeholderTextRes),
                style = TextStyle(color = Color.Gray, fontSize = FontSize.normalSize)
            )
        },
        supportingText = {
            if (isError) {
                Text(
                    text = errorMessage,
                    style = TextStyle(
                        color = Color.Red,
                        fontSize = FontSize.tinySize
                    )
                )
            }
        },
        isError = isError,
        colors = colors,
        keyboardOptions = keyboardOptions,
        value = value,
        onValueChange = onValueChanged
    )
}

@Composable
fun CommonInputFieldWithoutLeadingIcon(
    modifier: Modifier = Modifier,
    value: String,
    @StringRes placeholderTextRes: Int,
    colors: TextFieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color.White
    ),
    isError: Boolean = false,
    errorMessage: String = "账户或密码输入错误",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChanged: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(id = placeholderTextRes),
                style = TextStyle(color = Color.Gray, fontSize = FontSize.normalSize)
            )
        },
        isError = isError,
        supportingText = {
            if (isError) {
                Text(
                    text = errorMessage,
                    style = TextStyle(
                        color = Color.Red,
                        fontSize = FontSize.tinySize
                    )
                )
            }
        },
        colors = colors,
        keyboardOptions = keyboardOptions,
        value = value,
        onValueChange = onValueChanged
    )
}