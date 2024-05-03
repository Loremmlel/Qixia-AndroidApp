package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.BlurredBackground
import org.hinanawiyuzu.qixia.components.Fold
import org.hinanawiyuzu.qixia.components.GrayLine
import org.hinanawiyuzu.qixia.data.entity.TraceabilityAttribute
import org.hinanawiyuzu.qixia.data.entity.TraceabilityEssential
import org.hinanawiyuzu.qixia.data.entity.TraceabilityManufacturer
import org.hinanawiyuzu.qixia.data.entity.TraceabilityProduce
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.viewmodel.shared.SharedTraceabilityViewModel

// 展开信息中的灰色背景颜色
val infoGray = Color(0xFFF8F8F8)

@Composable
fun TraceabilityDetailScreen(
  modifier: Modifier = Modifier,
  sharedViewModel: SharedTraceabilityViewModel,
  navController: NavController
) {
  val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.BottomCenter,
  ) {
    BlurredBackground()
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      TopBar(
        modifier = Modifier.fillMaxWidth(),
        onBackClicked = { navController.popBackStack() }
      )
      GrayLine(screenWidthDp = screenWidthDp)
      Column(
        modifier = Modifier
          .verticalScroll(state = rememberScrollState())
          .padding(10.dp)
          .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
      ) {
        val traceability = sharedViewModel.traceability
        traceability?.let {
          Essential(
            modifier = Modifier.fillMaxWidth(),
            essential = it.essential
          )
          Produce(
            modifier = Modifier.fillMaxWidth(),
            produce = it.produce
          )
          it.attribute?.let { attribute ->
            Attribute(
              modifier = Modifier.fillMaxWidth(),
              attribute = attribute
            )
          }
          Manufacturer(
            modifier = Modifier.fillMaxWidth(),
            manufacturer = it.manufacturer
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
        painter = painterResource(id = R.drawable.left_arrow),
        contentDescription = "返回"
      )
    }
    Text(
      text = "药品追溯信息",
      style = TextStyle(
        fontSize = FontSize.veryLargeSize,
      )
    )
    Spacer(modifier = Modifier.size(40.dp))
  }
}

@Composable
private fun Essential(
  modifier: Modifier = Modifier,
  essential: TraceabilityEssential
) {
  Fold(
    modifier = modifier,
    text = "基本信息"
  ) {
    ExpandInfo(infoMap = essential.toDisplayedStringMap())
  }
}

@Composable
private fun Produce(
  modifier: Modifier = Modifier,
  produce: TraceabilityProduce
) {
  Fold(
    modifier = modifier,
    text = "生产信息"
  ) {
    ExpandInfo(infoMap = produce.toDisplayedStringMap())
  }
}

@Composable
private fun Attribute(
  modifier: Modifier = Modifier,
  attribute: TraceabilityAttribute
) {
  Fold(
    modifier = modifier,
    text = "类别属性"
  ) {
    ExpandInfo(infoMap = attribute.toDisplayedStringMap())
  }
}

@Composable
private fun Manufacturer(
  modifier: Modifier = Modifier,
  manufacturer: TraceabilityManufacturer
) {
  Fold(
    modifier = modifier,
    text = "厂商信息"
  ) {
    ExpandInfo(infoMap = manufacturer.toDisplayedStringMap())
  }
}

@Composable
private fun ExpandInfo(
  modifier: Modifier = Modifier,
  infoMap: Map<String, String>
) {
  var isOdd by remember { mutableStateOf(true) }
  Column(
    modifier = modifier
      .padding(bottom = 10.dp)
      .fillMaxWidth()
  ) {
    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(0.5.dp)
        .background(Color.Gray)
    )
    infoMap.forEach { (key, value) ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(if (isOdd) infoGray else Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = key,
          modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
          fontSize = FontSize.smallSize,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = value,
          modifier = Modifier.padding(end = 10.dp, top = 10.dp, bottom = 10.dp),
          fontSize = FontSize.smallSize,
          fontWeight = FontWeight.Bold
        )
      }
      isOdd = !isOdd
    }
  }
}