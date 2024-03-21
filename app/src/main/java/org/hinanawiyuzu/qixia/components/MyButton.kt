package org.hinanawiyuzu.qixia.components

import androidx.annotation.StringRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import org.hinanawiyuzu.qixia.data.FontSize
import org.hinanawiyuzu.qixia.ui.theme.secondary_color

/**
 * 普通按钮。默认是绿色的圆角矩形。
 * @param modifier 修饰符
 * @param buttonTextRes 按钮文本资源id
 * @param onButtonClicked 按钮点击回调函数
 * @param shape 按钮形状，默认是圆角矩形
 * @param colors 按钮颜色，默认是secondary_color。
 * @author HinanawiYuzu
 */
@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    @StringRes buttonTextRes: Int,
    onButtonClicked: () -> Unit,
    shape: Shape = RoundedCornerShape(percent = 15),
    colors: ButtonColors = ButtonDefaults.buttonColors(containerColor = secondary_color)
) {
    Button(
        modifier = modifier,
        shape = shape,
        colors = colors,
        onClick = onButtonClicked
    ) {
        Text(
            text = stringResource(id = buttonTextRes),
            style = TextStyle(fontSize = FontSize.loginScreenLoginButtonTextSize)
        )
    }
}