package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.BlurredBackground
import org.hinanawiyuzu.qixia.components.Fold
import org.hinanawiyuzu.qixia.components.GrayLine
import org.hinanawiyuzu.qixia.components.MyIconButton
import org.hinanawiyuzu.qixia.ui.AppViewModelProvider
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.viewmodel.RecordViewModel
import org.hinanawiyuzu.qixia.ui.viewmodel.TakeMedicineRecord
import org.hinanawiyuzu.qixia.utils.toDisplayChinese


@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
    changeBottomBarVisibility: (Boolean) -> Unit,
    viewModel: RecordViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.factory),
    navController: NavHostController = rememberNavController(),
) {
    val fontColor = Color(0xFF053B20)
    val blockColor = Color(0xFF8AD4AF)
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
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
                onMenuClicked = { }
            )
            GrayLine(screenWidthDp = screenWidthDp)
            Column(
                modifier = Modifier
                    .verticalScroll(state = rememberScrollState())
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .width(10.dp)
                            .height(40.dp)
                            .clip(RoundedCornerShape(percent = 30))
                            .background(blockColor)
                    ) {}
                    Text(
                        text = "用药记录",
                        color = fontColor,
                        fontSize = FontSize.mediumLargeSize
                    )
                }
                RecordList(
                    modifier = Modifier.fillMaxWidth(),
                    recordList = viewModel.takeMedicineRecord,
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    onMenuClicked: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MyIconButton(onClick = onMenuClicked) {
            Icon(
                painter = painterResource(id = R.drawable.top_bar_menu),
                contentDescription = "菜单",
                tint = Color.Black
            )
        }
        Text(
            text = "记录",
            style = TextStyle(
                fontSize = FontSize.mediumLargeSize,
            )
        )
        Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
private fun RecordList(
    modifier: Modifier = Modifier,
    recordList: List<TakeMedicineRecord>,
) {
    val containerColor = Color(0x70FFFFFF)
    val oddColor = Color(0x45CFF5D3)
    Fold(
        modifier = modifier,
        text = "用药记录(近三天)"
    ) {
        var isOdd by remember { mutableStateOf(false) }
        Column {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(percent = 20))
                    .fillMaxWidth()
                    .background(oddColor),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp, top = 5.dp, bottom = 5.dp),
                    text = "药物",
                    fontSize = FontSize.largeSize
                )
                Text(
                    modifier = Modifier,
                    text = "服用时间",
                    fontSize = FontSize.largeSize
                )
                Text(
                    modifier = Modifier,
                    text = "服用状态",
                    fontSize = FontSize.largeSize
                )
            }
            recordList.forEach {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 20))
                        .fillMaxWidth()
                        .background(if (isOdd) oddColor else containerColor),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isTaken = it.takeStatus.first.name == "Taken"
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp, top = 5.dp, bottom = 5.dp),
                        text = it.medicineName
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = 10.dp),
                        text = it.takeStatus.second!!.toDisplayChinese(),
                        color = if (isTaken) Color.Black else Color(0xFFCE2C2C),
                    )
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(percent = 40))
                            .background(if (isTaken) Color(0x3F33C821) else Color(0x3FC83F21))
                    ) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = if (isTaken) "按时服用" else "没有服用",
                            color = if (isTaken) Color.Black else Color(0xFFCE2C2C),
                        )
                    }
                }
                isOdd = !isOdd
            }
        }
    }
}