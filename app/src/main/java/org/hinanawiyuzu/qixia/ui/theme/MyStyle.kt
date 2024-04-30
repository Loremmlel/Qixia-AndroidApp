package org.hinanawiyuzu.qixia.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

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

    // 提醒界面日期卡片未选中边框渐变色
    val notSelectedCalendarCardBoardGradient: Brush = Brush.linearGradient(
        colors = listOf(neutral_color, primary_color)
    )

    // 提醒界面日期卡片选中边框渐变色
    val selectedCalendarCardBoardGradient: Brush = Brush.linearGradient(
        colors = listOf(Color.White, neutral_color)
    )

    // 提醒界面日期卡片选中内容渐变色
    val selectedCalendarCardContainerGradient: Brush = Brush.linearGradient(
        colors = listOf(Color(0xFF13BC68), Color(0xFF76FBB8)),
    )

    // 绿色卡片渐变色
    val greenCardGradient: Brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFFF4FFF5))
    )

    // 黄色卡片渐变色
    val yellowCardGradient: Brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFFFFF9E8))
    )

    // 红色卡片渐变色
    val redCardGradient: Brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFFFFF4F4))
    )

    // 会员色
    val vipBrush: Brush = Brush.linearGradient(
        colors = listOf(Color(0xFFFECE98), Color(0xFFF59A19))
    )

    val deepGreenButtonGradient: Brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xB2B4E3CC), Color(0xB25BC28F))
    )

    val transparentButtonBorderGradient: Brush = Brush.linearGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0x00FFFFFF))
    )

    val lightGreenCardGradient = Brush.radialGradient(colors = listOf(Color(0xFFFFFFFF), Color(0xFFF4FFFB)))

    val font_deep_blue: Color = Color(0xFF333E63)
}