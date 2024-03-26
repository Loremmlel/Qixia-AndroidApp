package org.hinanawiyuzu.qixia.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.data.MyColor

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = MyColor.themeHorizontalGradient)
    ) {
        val circleOffsetX = (-20).dp
        val circleOffsetY = 40.dp
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
            Scaffold(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStartPercent = 10, topEndPercent = 10))
                    .background(Color.White)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .requiredHeight((screenHeight * 0.81).dp),
                bottomBar = bottomBar,
                containerColor = Color.White
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStartPercent = 10, topEndPercent = 10))
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {

                }
            }
        }
    }
}