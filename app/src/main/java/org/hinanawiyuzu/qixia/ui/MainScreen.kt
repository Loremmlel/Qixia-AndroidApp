package org.hinanawiyuzu.qixia.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.data.FontSize
import org.hinanawiyuzu.qixia.data.MyColor
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.theme.primary_color
import org.hinanawiyuzu.qixia.ui.theme.secondary_color
import org.hinanawiyuzu.qixia.ui.theme.tertiary_color
import org.hinanawiyuzu.qixia.ui.viewmodel.MainViewModel
import org.hinanawiyuzu.qixia.utils.AppScreenState
import org.hinanawiyuzu.qixia.utils.advancedShadow

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
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
            Column(
                modifier = Modifier.offset(x = -circleOffsetX, y = circleOffsetY)
            ) {
                Text(
                    text = "早上好!",
                    style = TextStyle(
                        fontSize = FontSize.extraLargeSize,
                        fontWeight = FontWeight.Black
                    )
                )
                Text(
                    text = "陈女士",
                    style = TextStyle(
                        fontSize = FontSize.extraLargeSize,
                        fontWeight = FontWeight.Black
                    )
                )
            }
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
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Top(
                        modifier = modifier.weight(0.15f)
                    )
                    Middle(
                        modifier = modifier.weight(0.7f)
                    )
                    Bottom(
                        modifier = modifier.weight(0.15f)
                    )
                }
            }
        }
    }
}

@Composable
private fun Top(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.main_screen_document),
            contentDescription = "",
            tint = secondary_color
        )
        Text(
            modifier = Modifier
                .clip(RoundedCornerShape(percent = 10))
                .background(color = secondary_color)
                .padding(5.dp),
            text = "服药日程",
            style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = FontSize.bigSize,
            ),
            color = Color.White
        )
        Icon(
            painter = painterResource(id = R.drawable.volumn_down),
            contentDescription = null,
            tint = secondary_color
        )
    }
}

@Composable
private fun Middle(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ReminderCard(
            modifier = Modifier.weight(0.33f),
            pictureRes = R.drawable.bottle,
            time = "早上  7 : 00",
            medicine = "泮托拉唑钠肠溶片",
            usageAmount = "1片",
            usageMethod = "空腹"
        )
        Spacer(modifier = Modifier.size(5.dp))
        ReminderCard(
            modifier = Modifier.weight(0.33f),
            pictureRes = R.drawable.pills,
            time = "早上  8 : 00",
            medicine = "硫酸氢氯吡格雷片",
            usageAmount = "1片",
            usageMethod = "早饭后"
        )
        Spacer(modifier = Modifier.size(5.dp))
        ReminderCard(
            modifier = Modifier.weight(0.33f),
            pictureRes = R.drawable.shield,
            time = "晚上  6 : 00",
            medicine = "阿托伐他汀钙片",
            usageAmount = "1片",
            usageMethod = "晚饭后"
        )

    }
}

@Composable
private fun Bottom(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.calendar),
            contentDescription = null,
            tint = secondary_color
        )
        Icon(
            painter = painterResource(id = R.drawable.main_screen_plus),
            contentDescription = null,
            tint = secondary_color
        )
    }
}

@Composable
private fun ReminderCard(
    modifier: Modifier = Modifier,
    @DrawableRes pictureRes: Int,
    time: String,
    medicine: String,
    usageAmount: String,
    usageMethod: String
) {
    Card(
        modifier = modifier
            .padding(5.dp)
            .fillMaxWidth()
            .advancedShadow(
                alpha = 0.4f,
                cornersRadius = 25.dp,
                shadowBlurRadius = 5.dp,
                offsetY = 5.dp
            ),
        shape = RoundedCornerShape(percent = 25),
        colors = CardDefaults.cardColors(
            containerColor = tertiary_color
        )
    ) {
        Column {
            Text(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(start = 20.dp, top = 10.dp),
                text = time,
                style = TextStyle(
                    fontSize = FontSize.largeSize,
                    color = primary_color,
                    fontWeight = FontWeight.Bold
                )
            )
            Row(
                modifier = Modifier.weight(0.6f)
            ) {
                Image(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxHeight(),
                    painter = painterResource(id = pictureRes),
                    contentDescription = medicine
                )
                Column(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .weight(0.8f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = medicine,
                        style = TextStyle(
                            fontSize = FontSize.mediumLargeSize,
                            color = Color.White
                        ),
                        fontWeight = FontWeight.ExtraBold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = usageAmount,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                        Text(
                            text = usageMethod,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    QixiaTheme {
        MainScreen(bottomBar = {
            BottomAppBar(
                onBottomBarItemClicked = {},
                barIconSelectedStatements = AppScreenState.Main
            )
        })
    }
}