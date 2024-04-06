package org.hinanawiyuzu.qixia.components

import androidx.annotation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.*
import org.hinanawiyuzu.qixia.ui.theme.*

/**
 * 普通按钮。默认是绿色的圆角矩形。
 * @param modifier 修饰符
 * @param buttonTextRes 按钮文本资源id
 * @param onButtonClicked 按钮点击回调函数
 * @param shape 按钮形状，默认是圆角矩形
 * @param buttonColors 按钮颜色，默认是secondary_color。
 * @author HinanawiYuzu
 */
@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    @StringRes buttonTextRes: Int,
    onButtonClicked: () -> Unit,
    shape: Shape = RoundedCornerShape(percent = 15),
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(containerColor = secondary_color),
    fontColors: Color = Color.White,
    fontSize: TextUnit = FontSize.loginScreenLoginButtonTextSize,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier,
        shape = shape,
        colors = buttonColors,
        enabled = enabled,
        onClick = onButtonClicked
    ) {
        Text(
            text = stringResource(id = buttonTextRes),
            style = TextStyle(
                fontSize = fontSize,
                color = fontColors
            )
        )
    }
}

/**
 * 去除了点击黑框特效的IconButton
 */
@Composable
fun MyIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    // 这也是源码的一部分
    val iconButtonSizeModifier = Modifier.size(40.dp)
    Box(
        modifier = modifier
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null
            )
            .then(iconButtonSizeModifier),
        contentAlignment = Alignment.Center
    ) { content() }
}