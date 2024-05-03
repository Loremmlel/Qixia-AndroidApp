package org.hinanawiyuzu.qixia.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.hinanawiyuzu.qixia.components.BlurredBackground
import org.hinanawiyuzu.qixia.components.GrayLine
import org.hinanawiyuzu.qixia.data.source.fake.fakeHealthCalendar
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.utils.toBitmap
import java.time.LocalDate

private val titleFontColor = Color(0xFF78984F)
private val contentFontColor = Color(0xFF59597C)

@Composable
fun HealthCalendarScreen(
  modifier: Modifier = Modifier,
  navController: NavController,
) {
  val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
  val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
  val healthCalendar = fakeHealthCalendar.find { it.month == LocalDate.now().monthValue }
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.BottomCenter
  ) {
    BlurredBackground()
    Column(
      modifier = modifier
        .fillMaxSize()
        .align(Alignment.TopCenter),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      TopBar(
        modifier = Modifier.fillMaxWidth(),
        onBackClicked = { navController.popBackStack() }
      )
      GrayLine(screenWidthDp = screenWidthDp)
      Column(
        modifier = Modifier
          .verticalScroll(
            state = rememberScrollState(),
          ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
      ) {
        healthCalendar?.let {
          Head(
            modifier = Modifier
              .padding(start = 20.dp, bottom = 20.dp)
              .height(screenHeightDp / 3)
              .fillMaxWidth(),
            month = it.month,
            title = it.title,
            imageResId = it.imageResId
          )
          TitleAndCard(
            modifier = Modifier
              .padding(horizontal = 20.dp)
              .fillMaxWidth(),
            title = "养生原则",
            content = it.principle
          )
          TitleAndCard(
            modifier = Modifier
              .padding(horizontal = 20.dp)
              .fillMaxWidth(),
            title = "注意事项",
            content = it.matters
          )
          TitleAndCard(
            modifier = Modifier
              .padding(horizontal = 20.dp)
              .fillMaxWidth(),
            title = "饮食良方",
            content = it.diet
          )
        }
      }
    }
  }
}

@Composable
private fun TopBar(
  modifier: Modifier = Modifier,
  onBackClicked: () -> Unit
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    IconButton(onClick = onBackClicked) {
      Icon(
        painter = painterResource(id = org.hinanawiyuzu.qixia.R.drawable.left_arrow),
        contentDescription = "返回"
      )
    }
    Text(
      text = "养生日历",
      style = TextStyle(
        fontSize = FontSize.veryLargeSize,
      )
    )
    Spacer(modifier = Modifier.size(40.dp))
  }
}

@Composable
@Stable
private fun Head(
  modifier: Modifier = Modifier,
  month: Int,
  title: String,
  @DrawableRes imageResId: Int
) {
  val context = LocalContext.current
  val chineseMonth = when (month) {
    1 -> "一月"
    2 -> "二月"
    3 -> "三月"
    4 -> "四月"
    5 -> "五月"
    6 -> "六月"
    7 -> "七月"
    8 -> "八月"
    9 -> "九月"
    10 -> "十月"
    11 -> "十一月"
    12 -> "十二月"
    else -> "七月"
  }
  Box(
    modifier = modifier
  ) {
    Image(
      modifier = Modifier
        .fillMaxSize()
        .align(Alignment.BottomEnd),
      bitmap = imageResId.toBitmap(context).asImageBitmap(),
      contentDescription = null,
      contentScale = ContentScale.FillWidth
    )
    Row(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(start = 20.dp, bottom = 20.dp),
      verticalAlignment = Alignment.Bottom
    ) {
      Text(
        text = chineseMonth,
        color = titleFontColor,
        fontSize = 50.sp,
        fontWeight = FontWeight.Black
      )
      Text(
        text = "    ${title.toCharArray().joinToString(" ")}",
        color = titleFontColor,
        fontSize = FontSize.mediumLargeSize
      )
    }
  }
}

@Composable
private fun TitleAndCard(
  modifier: Modifier = Modifier,
  title: String,
  content: String
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(5.dp)
  ) {
    Text(
      text = title,
      color = titleFontColor,
      fontSize = FontSize.mediumLargeSize,
      fontWeight = FontWeight.ExtraBold
    )
    Row(
      modifier = Modifier
        .clip(RoundedCornerShape(percent = 15))
        .fillMaxWidth()
        .background(brush = Brush.radialGradient(colors = listOf(Color(0xFFFFFFFF), Color(0xFFF4FFFB)))),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        modifier = Modifier
          .padding(horizontal = 20.dp, vertical = 10.dp)
          .fillMaxSize(),
        text = content,
        color = contentFontColor,
      )
    }
  }
}