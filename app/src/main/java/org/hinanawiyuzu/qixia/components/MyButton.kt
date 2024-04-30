package org.hinanawiyuzu.qixia.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.theme.MyColor
import org.hinanawiyuzu.qixia.ui.theme.primary_color
import org.hinanawiyuzu.qixia.ui.theme.secondary_color
import org.hinanawiyuzu.qixia.utils.advancedShadow

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
    icon: @Composable () -> Unit
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
    ) { icon() }
}

/**
 * 中间有白圈的绿色渐变色按钮。因为普通的按钮无法应用渐变色所以只能用modifier.clickable了
 * @param modifier 修饰符
 * @param text 按钮中显示的文字
 * @param textStyle 文字的样式
 * @param onClick 点击事件
 */
@Composable
fun GreenGradientButton(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = TextStyle(),
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .clip(RoundedCornerShape(percent = 20))
            .background(brush = MyColor.deepGreenButtonGradient),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = textStyle
        )
    }
}

/**
 * 这就是上面提到的绿色箭头。下面还会有两个模块要用到这玩意儿。
 * @param modifier 修饰符
 * @param onClicked 点击事件
 * @author HinanawiYuzu
 */
@Composable
fun GreenArrow(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    MyIconButton(
        modifier = modifier,
        onClick = onClicked
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .advancedShadow(
                        color = primary_color,
                        alpha = 0.2f,
                        shadowBlurRadius = 5.dp,
                        cornersRadius = 10.dp,
                        offsetY = 5.dp
                    ),
                painter = painterResource(id = R.drawable.remind_screen_rec_back),
                contentDescription = null
            )
            Image(
                painter = painterResource(id = R.drawable.remind_screen_rec_arrow),
                contentDescription = null
            )
        }
    }
}