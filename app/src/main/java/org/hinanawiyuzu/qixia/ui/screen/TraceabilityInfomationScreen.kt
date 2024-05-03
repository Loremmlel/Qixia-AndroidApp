package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.yxing.ScanCodeConfig
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.BlurredBackground
import org.hinanawiyuzu.qixia.components.GrayLine
import org.hinanawiyuzu.qixia.components.GreenGradientButton
import org.hinanawiyuzu.qixia.data.entity.Traceability
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.viewmodel.TraceabilityInformationViewModel
import org.hinanawiyuzu.qixia.ui.viewmodel.shared.SharedTraceabilityViewModel
import org.hinanawiyuzu.qixia.utils.makeWhiteTransparent
import org.hinanawiyuzu.qixia.utils.toBitmap

@Composable
fun TraceabilityInformationScreen(
  modifier: Modifier = Modifier,
  viewModel: TraceabilityInformationViewModel = viewModel(),
  sharedViewModel: SharedTraceabilityViewModel,
  navController: NavHostController = rememberNavController(),
  backStackEntry: NavBackStackEntry
) {
  val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
  val code = backStackEntry.arguments?.getString("code")
  code?.let {
    viewModel.code = it
    viewModel.searchTraceability()
  }
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.BottomCenter
  ) {
    BlurredBackground()
    Column(
      modifier = modifier.fillMaxSize()
    ) {
      TopBar(
        modifier = Modifier.fillMaxWidth(),
        onBackClicked = { navController.popBackStack() }
      )
      GrayLine(screenWidthDp = screenWidthDp)
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
      ) {
        viewModel.traceability?.let {
          VerificationCard(
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp),
            barCode = viewModel.code!!,
            traceability = it
          )
          MedicineCard(
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp),
            traceability = it,
            onCheckDetailButtonClicked = {
              viewModel.onCheckDetailButtonClicked(
                navController = navController,
                sharedViewModel = sharedViewModel
              )
            },
            onAddToMedicineClicked = {
              viewModel.onAddToMedicineClicked(
                navController = navController,
                sharedViewModel = sharedViewModel
              )
            }
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
      text = "扫码结果",
      style = TextStyle(
        fontSize = FontSize.veryLargeSize,
      )
    )
    Spacer(modifier = Modifier.size(40.dp))
  }
}

@Composable
private fun VerificationCard(
  modifier: Modifier = Modifier,
  barCode: String,
  traceability: Traceability
) {
  val warn: Boolean = traceability.essential.numberOfPeople > 1
  // 药品被多人扫描后的卡片颜色
  val warnColor = Color(0xFFFDF9EC)
  // 药品被一人扫描后的卡片颜色
  val normalColor = Color(243, 249, 247, 0xAA)
  val barCodeBitmap = ScanCodeConfig.createBarCode(
    barCode,
    700,
    100,
    true
  ).makeWhiteTransparent()
  Column(
    modifier = modifier
      .clip(RoundedCornerShape(percent = 10))
      .background(color = if (warn) warnColor else normalColor)
  ) {
    Column(
      modifier = Modifier
        .padding(10.dp)
        .weight(1f)
        .fillMaxWidth()
    ) {
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = "药品唯一身份标识码",
        letterSpacing = 5.sp,
        textAlign = TextAlign.Center,
        fontSize = FontSize.smallSize
      )
      Image(
        modifier = Modifier.fillMaxWidth(),
        bitmap = barCodeBitmap.asImageBitmap(),
        contentDescription = null
      )
    }
    Column(
      modifier = Modifier
        .weight(0.7f)
        .padding(start = 30.dp, bottom = 20.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = if (warn) "药品被多人验证" else "药品追溯码验证通过",
        color = if (warn) Color(0xFFFFB000) else Color(18, 190, 161),
        fontSize = FontSize.largeSize
      )
      Row {
        Text(
          text = "药品被 "
        )
        Text(
          text = "${traceability.essential.numberOfPeople}",
          color = if (warn) Color(0xFFFFB000) else Color(18, 190, 161)
        )
        Text(
          text = " 人扫码查询"
        )
      }
    }
  }
}

@Composable
private fun MedicineCard(
  modifier: Modifier = Modifier,
  traceability: Traceability,
  onCheckDetailButtonClicked: () -> Unit,
  onAddToMedicineClicked: () -> Unit
) {
  Column(
    modifier = modifier
      .clip(RoundedCornerShape(percent = 10))
      .background(Color.White)
  ) {
    Row(
      modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .weight(2f)
    ) {
      Column(
        modifier = Modifier.weight(0.7f)
      ) {
        Text(
          text = traceability.essential.chineseCommonName,
          fontSize = FontSize.veryLargeSize
        )
        Text(
          text = "包装规格: ${traceability.essential.packingSpecification}",
          fontSize = FontSize.smallSize,
          fontWeight = FontWeight.Light
        )
        Text(
          text = "生产厂家: ${traceability.manufacturer.manufacturerName}",
          fontSize = FontSize.smallSize,
          fontWeight = FontWeight.Light
        )
      }
      Image(
        modifier = Modifier.weight(0.3f),
        bitmap =
        R.drawable.default_medicine_png.toBitmap(LocalContext.current).asImageBitmap(),
        contentDescription = null,
        contentScale = ContentScale.Fit
      )
    }
    Row(
      modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .weight(1f),
      verticalAlignment = Alignment.CenterVertically
    ) {
      GreenGradientButton(
        modifier = Modifier
          .fillMaxHeight()
          .weight(1f),
        text = "药品追溯信息"
      ) { onCheckDetailButtonClicked() }
      Spacer(modifier = Modifier.weight(0.2f))
      GreenGradientButton(
        modifier = Modifier
          .fillMaxHeight()
          .weight(1f),
        text = "添加到药箱"
      ) { onAddToMedicineClicked() }
    }
  }
}