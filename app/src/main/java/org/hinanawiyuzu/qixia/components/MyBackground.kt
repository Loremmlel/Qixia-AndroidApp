package org.hinanawiyuzu.qixia.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.data.MyColor

/**
 * 默认的背景样式。即横向渐变色+两个alpha值为0.3的白色圆圈。
 * @param modifier 修饰符
 * @param screenHeight 屏幕高度dp，建议在对应的ViewModel里定义并获取。
 * @param content 要加入的内容。占屏幕高度的0.8空间。
 * @author HinanawiYuzu
 */
@Composable
fun CommonBackground(
    modifier: Modifier = Modifier,
    screenHeight: Dp,
    content: @Composable (screenHeight: Dp) -> Unit
) {
    val circleOffsetX = (-20).dp
    val circleOffsetY = 50.dp
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = MyColor.themeHorizontalGradient)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxSize()
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = circleOffsetX, y = circleOffsetY),
                painter = painterResource(id = R.drawable.background_circle),
                contentDescription = null
            )
            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = circleOffsetX + 30.dp, y = circleOffsetY + 45.dp),
                painter = painterResource(id = R.drawable.background_circle),
                contentDescription = null
            )
        }
        content(screenHeight)
    }
}