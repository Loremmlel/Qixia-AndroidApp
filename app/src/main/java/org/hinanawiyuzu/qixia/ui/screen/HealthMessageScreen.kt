package org.hinanawiyuzu.qixia.ui.screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.BlurredBackground
import org.hinanawiyuzu.qixia.components.GrayLine
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.viewmodel.HealthMessageViewModel
import org.hinanawiyuzu.qixia.utils.toBitmap

@Composable
fun HealthMessageScreen(
    modifier: Modifier = Modifier,
    viewModel: HealthMessageViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavController,
    backStackEntry: NavBackStackEntry
) {
    val context = LocalContext.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    viewModel.healthMessageId = backStackEntry.arguments?.getInt("healthMessageId")
    val messageBitmap = R.drawable.health_message_example_1.toBitmap(context)
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        BlurredBackground()
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                onBackClicked = { navController.popBackStack() }
            )
            GrayLine(screenWidthDp = screenWidthDp)
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .verticalScroll(
                        state = rememberScrollState(),
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HealthMessage(
                    modifier = Modifier.fillMaxSize(),
                    messageAsset = messageBitmap
                )
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
            text = "健康资讯",
            style = TextStyle(
                fontSize = FontSize.veryLargeSize,
            )
        )
        Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
private fun HealthMessage(
    modifier: Modifier = Modifier,
    messageAsset: Bitmap? = null //TODO:暂时设定为位图吧……
) {
    val title = "冷天容易血压高，多吃降压药？｜科学排雷"
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            fontSize = FontSize.bigSize
        )
        Image(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .fillMaxSize(),
            bitmap = messageAsset?.asImageBitmap() ?: ImageBitmap(1, 1),
            contentDescription = title,
            contentScale = ContentScale.FillWidth,
        )
    }
}