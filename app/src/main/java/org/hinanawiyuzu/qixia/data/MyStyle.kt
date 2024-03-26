package org.hinanawiyuzu.qixia.data

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import org.hinanawiyuzu.qixia.ui.theme.neutral_color
import org.hinanawiyuzu.qixia.ui.theme.secondary_color
import org.hinanawiyuzu.qixia.ui.theme.tertiary_color

object FontSize {
    val tinySize = 12.sp
    val smallSize = 14.sp
    val normalSize = 16.sp
    val bigSize = 18.sp
    val largeSize = 20.sp
    val mediumLargeSize = 22.sp
    val veryLargeSize = 24.sp
    val extraLargeSize = 30.sp
    val loginScreenLoginSize = 35.sp
    val loginScreenForgetPasswordSize = 20.sp
    val loginScreenOrSize = 20.sp
    val loginScreenLoginButtonTextSize = 30.sp
    val loginScreenThirdPartySize = 20.sp
    val verificationScreenLargeSize = 30.sp
}

object MyColor {
    // 常用主体水平渐变背景色。
    val themeHorizontalGradient: Brush = Brush.horizontalGradient(
        colors = listOf(secondary_color, tertiary_color, neutral_color)
    )

    // 底部导航栏动画圆球渐变色
    val bottomAnimatedCircleGradient: Brush = Brush.linearGradient(
        colors = listOf(Color(0xFF5BC28F), Color(0xFFC7FFE3))
    )
}