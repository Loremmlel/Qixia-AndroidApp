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
 * 使用模糊效果的背景。
 */
@Stable
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