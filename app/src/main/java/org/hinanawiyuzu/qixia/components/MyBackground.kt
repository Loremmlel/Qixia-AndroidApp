package org.hinanawiyuzu.qixia.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.ui.theme.*

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


/**
 * 使用模糊效果的背景。
 */
@Composable
fun BlurredBackground(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            // 2的饱和度更高，颜色更鲜艳
            painter = painterResource(id = R.drawable.blurred_background40),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
    }
}


@Composable
fun GrayLine(
    modifier: Modifier = Modifier,
    screenWidthDp: Dp,
) {
    Spacer(modifier = Modifier.size(5.dp))
    Column(
        modifier = modifier
            .requiredWidth(screenWidthDp)
            .height(1.dp)
            .background(Color.LightGray)
    ) {}
}

@Preview
@Composable
fun BackgroundPreview() {
    QixiaTheme {
        BlurredBackground()
    }
}