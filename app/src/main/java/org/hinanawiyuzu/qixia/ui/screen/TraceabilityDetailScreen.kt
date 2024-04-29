package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.*
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.ui.theme.*
import org.hinanawiyuzu.qixia.ui.viewmodel.shared.*

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