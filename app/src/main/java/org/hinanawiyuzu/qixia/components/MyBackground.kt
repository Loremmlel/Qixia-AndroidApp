package org.hinanawiyuzu.qixia.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme


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