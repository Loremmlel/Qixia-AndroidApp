package org.hinanawiyuzu.qixia.ui.screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.GrayLine
import org.hinanawiyuzu.qixia.components.MyIconButton
import org.hinanawiyuzu.qixia.ui.AppViewModelProvider
import org.hinanawiyuzu.qixia.ui.route.BoxRoute
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.theme.MyColor
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.viewmodel.BoxViewModel
import org.hinanawiyuzu.qixia.utils.slideComposable
import org.hinanawiyuzu.qixia.utils.toBitmap
import java.text.DecimalFormat
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private const val CARD_HEIGHT = 60
val greenTint = Color(0xFF00BA61)
private val fontColor = Color(0xFF053B20)

@Composable
fun BoxScreen(
    modifier: Modifier = Modifier,
    changeBottomBarVisibility: (Boolean) -> Unit,
    viewModel: BoxViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.factory),
    navController: NavHostController = rememberNavController(),
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val currentLoginUser by viewModel.currentUser.collectAsState()
    // 模拟温度和湿度变化
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            viewModel.currentTemperature += (Random.nextFloat() - 0.5f)
            viewModel.currentHumidity += (Random.nextFloat() * 2 - 1f)
        }
    }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = BoxRoute.BoxScreen.name
    ) {
        composable(
            route = BoxRoute.BoxScreen.name,
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(500), targetOffsetX = { -it })
            },
            enterTransition = {
                slideInHorizontally(animationSpec = tween(500), initialOffsetX = { it })
            },
            // pop可以控制用户按返回键的动画，即NavigateUp,同Navigate区分开来。
            popEnterTransition = {
                slideInHorizontally(animationSpec = tween(500), initialOffsetX = { -it })
            }
        ) {
            changeBottomBarVisibility(true)
            Column(
                modifier = Modifier
                    .padding(15.dp)
            ) {
                TopBar(
                    modifier = Modifier.fillMaxWidth(),
                    profilePhotoUri = null,
                    onMenuClicked = {},
                    onProfilePhotoClicked = {}
                )
                GrayLine(screenWidthDp = screenWidthDp)
                Column(
                    modifier = Modifier.padding(top = 20.dp, start = 10.dp, end = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (currentLoginUser.user.isNotEmpty()) {
                        BluetoothLink(
                            userName = currentLoginUser.user.first().name.slice(0..<1) +
                                    if (currentLoginUser.user.first().sexual == "男") "先生" else "女士",
                        )
                    }
                    AnimateTempDisplay(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(300.dp),
                        currentTemperature = viewModel.currentTemperature,
                        currentHumidity = viewModel.currentHumidity
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row {
                            ClickButton(
                                modifier = Modifier.weight(1f),
                                iconRes = R.drawable.box_screen_medicine_list,
                                text = "查看药品清单",
                                onClick = { navController.navigate(BoxRoute.MedicineRepoScreen.name) }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            ClickButton(
                                modifier = Modifier.weight(1f),
                                iconRes = R.drawable.box_screen_add_medicine,
                                text = "新增药品",
                                onClick = {}
                            )
                        }
                        Row {
                            ClickButton(
                                modifier = Modifier.weight(1f),
                                iconRes = R.drawable.box_screen_scan_medicine,
                                text = "扫一扫药品",
                                onClick = {}
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            ClickButton(
                                modifier = Modifier.weight(1f),
                                iconRes = R.drawable.box_screen_replenish_medicine,
                                text = "补充药量",
                                onClick = {}
                            )
                        }
                    }
                }
            }
        }
        slideComposable(
            route = BoxRoute.MedicineRepoScreen.name,
        ) {
            changeBottomBarVisibility(false)
            MedicineRepoListScreen(navController = navController)
        }
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    profilePhotoUri: Uri? = null,
    onMenuClicked: () -> Unit,
    onProfilePhotoClicked: () -> Unit
) {
    val context = LocalContext.current
    var profilePhotoBitmap: Bitmap? by remember { mutableStateOf(null) }
    LaunchedEffect(profilePhotoUri) {
        profilePhotoBitmap = coroutineScope {
            async(Dispatchers.IO) {
                profilePhotoUri?.toBitmap(context) ?: R.drawable.default_profile_photo_female.toBitmap(context)
            }.await()
        }
    }
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
            text = "主页",
            style = TextStyle(
                fontSize = FontSize.mediumLargeSize,
            )
        )
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onProfilePhotoClicked() }
                .background(color = Color(0xFFF4F8F2))
                .size(40.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                bitmap = profilePhotoBitmap?.asImageBitmap() ?: ImageBitmap(1, 1),
                contentDescription = "头像",
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
private fun BluetoothLink(
    modifier: Modifier = Modifier,
    userName: String,
    linkState: Boolean = true
) {
    val blockColor = Color(0xFF8AD4AF)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(30.dp)
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
                modifier = Modifier.weight(5f),
                text = "药箱连接",
                color = fontColor,
                fontSize = FontSize.mediumLargeSize
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bluetooth),
                    contentDescription = "蓝牙连接"
                )
                Text(
                    text = "${userName}的祺匣",
                    color = fontColor,
                    fontSize = FontSize.largeSize
                )
            }
            Image(
                painter = painterResource(id = if (linkState) R.drawable.box_screen_link else R.drawable.box_screen_unlink),
                contentDescription = "连接状态"
            )
        }
    }
}

/**
 * 温度显示
 */
@Composable
private fun AnimateTempDisplay(
    modifier: Modifier = Modifier,
    currentTemperature: Float = 24f,
    currentHumidity: Float = 50f
) {
    val density = LocalDensity.current.density
    val minTemperature = 16f
    val maxTemperature = 32f
    val middleTemperature = (minTemperature + maxTemperature) / 2
    val normalizedTemperature = (currentTemperature - minTemperature) / (maxTemperature - minTemperature)
    val sweepAngle by animateFloatAsState(
        targetValue = normalizedTemperature * 180f,
        animationSpec = tween(durationMillis = 1000),
        label = "圆弧外绿线动画"
    )
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(0.85f)
        ) {
            val circleColor = Color.White
            val outerCircleColor = Color(0xFFE4F7E4)
            val outerCircleGradient = Brush.radialGradient(colors = listOf(Color.LightGray, outerCircleColor))
            val outerRadius = size.minDimension / 2 - 20 * density
            val innerRadius = outerRadius - 20 * density
            val center = Offset(size.width / 2, size.height / 2)
            drawArc(
                brush = outerCircleGradient,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = 40 * density),
                topLeft = Offset(center.x - innerRadius, center.y - innerRadius),
                size = Size(innerRadius * 2, innerRadius * 2)
            )
            //            drawArc(
            //                brush = shadowGradient,
            //                startAngle = 0f,
            //                sweepAngle = 360f,
            //                useCenter = false,
            //                style = Stroke(width = 10 * density),
            //                topLeft = Offset(center.x - innerRadius, center.y - innerRadius),
            //                size = Size(innerRadius * 2, innerRadius * 2)
            //            )
            drawCircle(
                color = circleColor,
                radius = innerRadius,
                center = center
            )
            drawArc(
                color = greenTint,
                startAngle = 180f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 6 * density),
                topLeft = Offset(center.x - outerRadius, center.y - outerRadius),
                size = Size(outerRadius * 2, outerRadius * 2)
            )
            val startCircleCenterOffset = Offset(center.x - outerRadius, center.y)
            val startCircleRadius = 8 * density
            drawCircle(
                color = greenTint,
                radius = startCircleRadius,
                center = startCircleCenterOffset,
                style = Stroke(width = 2 * density)
            )
            // 绘制外围刻度线
            val numberOfTicks = ((maxTemperature - minTemperature) * 1.5).toInt()
            val tickColor = Color.Black
            val tickRadius = outerRadius + 6 * density
            for (i in 0..numberOfTicks) {
                val tickLength = if (i % 4 != 0) 5 * density else 10 * density
                val angle = Math.toRadians((i * 270.0 / numberOfTicks + 135))
                val startTick = Offset(
                    x = (center.x + tickRadius * cos(angle)).toFloat(),
                    y = (center.y + tickRadius * sin(angle)).toFloat()
                )
                val endTick = Offset(
                    x = (center.x + (tickRadius + tickLength) * cos(angle)).toFloat(),
                    y = (center.y + (tickRadius + tickLength) * sin(angle)).toFloat()
                )
                drawLine(
                    color = tickColor,
                    start = startTick,
                    end = endTick,
                    // 每四个刻度线加粗
                    strokeWidth = if (i % 4 != 0) 2f else 4f
                )
            }
            //            val endAngleRad = Math.toRadians((180f + sweepAngle).toDouble())
            //            val endPoint = Offset(
            //                x = (center.x + outerRadius * cos(endAngleRad)).toFloat(),
            //                y = (center.y + outerRadius * sin(endAngleRad)).toFloat()
            //            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = DecimalFormat("#.#").format(currentTemperature),
                    fontWeight = FontWeight.Black,
                    fontSize = 42.sp
                )
                Text(
                    text = "°C",
                    fontWeight = FontWeight.Black,
                    fontSize = FontSize.bigSize
                )
            }
            Text(
                text = "药箱内温度",
                fontSize = FontSize.mediumLargeSize
            )
        }
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = "${minTemperature.toInt()}",
            color = greenTint,
            fontSize = FontSize.mediumLargeSize,
            fontWeight = FontWeight.Black
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter),
            text = "${middleTemperature.toInt()}",
            color = greenTint,
            fontSize = FontSize.mediumLargeSize,
            fontWeight = FontWeight.Black
        )
        Text(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = "${maxTemperature.toInt()}",
            color = greenTint,
            fontSize = FontSize.mediumLargeSize,
            fontWeight = FontWeight.Black
        )
        TempAndHumidityDisplay(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.9f),
            currentTemperature = currentTemperature,
            currentHumidity = currentHumidity
        )
    }
}

@Composable
private fun TempAndHumidityDisplay(
    modifier: Modifier = Modifier,
    currentTemperature: Float = 24f,
    currentHumidity: Float = 50f
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            Image(
                modifier = Modifier.align(Alignment.Top),
                painter = painterResource(id = R.drawable.humidity),
                contentDescription = "湿度"
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "当前湿度",
                    color = fontColor,
                    fontSize = FontSize.largeSize
                )
                Text(
                    text = "${currentHumidity.toInt()}%",
                    color = fontColor,
                    fontSize = FontSize.largeSize,
                    fontWeight = FontWeight.Black
                )
            }
        }
        Spacer(
            modifier = Modifier
                .width(1.dp)
                .height(30.dp)
                .background(Color.LightGray)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            Image(
                modifier = Modifier.align(Alignment.Top),
                painter = painterResource(id = R.drawable.temperature),
                contentDescription = "温度"
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "当前温度",
                    color = fontColor,
                    fontSize = FontSize.largeSize
                )
                Text(
                    text = "${currentTemperature.toInt()}°C",
                    color = fontColor,
                    fontSize = FontSize.largeSize,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
private fun ClickButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .clip(RoundedCornerShape(percent = 15))
            .fillMaxWidth()
            .height(CARD_HEIGHT.dp)
            .background(brush = MyColor.lightGreenCardGradient),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            contentScale = ContentScale.Inside
        )
        Text(
            text = text,
            color = fontColor,
            fontSize = FontSize.normalSize
        )
    }
}

//暂时不实现箭头了
//fun DrawScope.drawTemperatureArrow(
//    rotate: Float = 0f,
//    startPoint: Offset = Offset(0f, 0f),
//    scale: Float = 1f
//) {
//    rotate(rotate, pivot = startPoint) {
//        val path1 = Path().apply {
//            moveTo(startPoint.x + 12.639f * scale, startPoint.y + 5.943f * scale)
//            cubicTo(
//                startPoint.x + 12.639f * scale,
//                startPoint.y + 5.943f * scale,
//                startPoint.x + 6.228f * scale,
//                startPoint.y + 15.948f * scale,
//                startPoint.x + 6.228f * scale,
//                startPoint.y + 19.557f * scale
//            )
//            cubicTo(
//                startPoint.x + 6.228f * scale,
//                startPoint.y + 23.166f * scale,
//                startPoint.x + 9.094f * scale,
//                startPoint.y + 26.091f * scale,
//                startPoint.x + 12.639f * scale,
//                startPoint.y + 26.091f * scale
//            )
//            cubicTo(
//                startPoint.x + 16.183f * scale,
//                startPoint.y + 26.091f * scale,
//                startPoint.x + 19.049f * scale,
//                startPoint.y + 23.166f * scale,
//                startPoint.x + 19.049f * scale,
//                startPoint.y + 19.557f * scale
//            )
//            cubicTo(
//                startPoint.x + 19.049f * scale,
//                startPoint.y + 15.948f * scale,
//                startPoint.x + 12.639f * scale,
//                startPoint.y + 5.943f * scale,
//                startPoint.x + 12.639f * scale,
//                startPoint.y + 5.943f * scale
//            )
//            close()
//        }
//        drawPath(path = path1, color = Color.White)
//        val path2 = Path().apply {
//            fillType = PathFillType.EvenOdd
//            moveTo(startPoint.x + 12.639f * scale, startPoint.y + 2.548f * scale)
//            lineTo(startPoint.x + 11.101f * scale, startPoint.y + 4.954f * scale)
//            cubicTo(
//                startPoint.x + 10.008f * scale,
//                startPoint.y + 6.659f * scale,
//                startPoint.x + 8.915f * scale,
//                startPoint.y + 8.515f * scale,
//                startPoint.x + 7.822f * scale,
//                startPoint.y + 10.523f * scale
//            )
//            cubicTo(
//                startPoint.x + 5.538f * scale,
//                startPoint.y + 14.731f * scale,
//                startPoint.x + 4.396f * scale,
//                startPoint.y + 17.743f * scale,
//                startPoint.x + 4.396f * scale,
//                startPoint.y + 19.557f * scale
//            )
//            cubicTo(
//                startPoint.x + 4.396f * scale,
//                startPoint.y + 24.11f * scale,
//                startPoint.x + 8.087f * scale,
//                startPoint.y + 27.923f * scale,
//                startPoint.x + 12.639f * scale,
//                startPoint.y + 27.923f * scale
//            )
//            cubicTo(
//                startPoint.x + 17.264f * scale,
//                startPoint.y + 27.923f * scale,
//                startPoint.x + 20.882f * scale,
//                startPoint.y + 24.177f * scale,
//                startPoint.x + 20.882f * scale,
//                startPoint.y + 19.557f * scale
//            )
//            cubicTo(
//                startPoint.x + 20.882f * scale,
//                startPoint.y + 17.743f * scale,
//                startPoint.x + 19.74f * scale,
//                startPoint.y + 14.731f * scale,
//                startPoint.x + 17.457f * scale,
//                startPoint.y + 10.523f * scale
//            )
//            cubicTo(
//                startPoint.x + 16.364f * scale,
//                startPoint.y + 8.515f * scale,
//                startPoint.x + 15.271f * scale,
//                startPoint.y + 6.659f * scale,
//                startPoint.x + 14.178f * scale,
//                startPoint.y + 4.954f * scale
//            )
//            lineTo(startPoint.x + 12.639f * scale, startPoint.y + 2.548f * scale)
//            moveTo(startPoint.x + 11.568f * scale, startPoint.y + 7.677f * scale)
//            cubicTo(
//                startPoint.x + 12.209f * scale,
//                startPoint.y + 6.617f * scale,
//                startPoint.x + 12.639f * scale,
//                startPoint.y + 5.943f * scale,
//                startPoint.x + 12.639f * scale,
//                startPoint.y + 5.943f * scale
//            )
//            cubicTo(
//                startPoint.x + 12.639f * scale,
//                startPoint.y + 5.943f * scale,
//                startPoint.x + 13.07f * scale,
//                startPoint.y + 6.617f * scale,
//                startPoint.x + 13.711f * scale,
//                startPoint.y + 7.677f * scale
//            )
//            cubicTo(
//                startPoint.x + 15.533f * scale,
//                startPoint.y + 10.704f * scale,
//                startPoint.x + 19.05f * scale,
//                startPoint.y + 16.884f * scale,
//                startPoint.x + 19.05f * scale,
//                startPoint.y + 19.557f * scale
//            )
//            cubicTo(
//                startPoint.x + 19.05f * scale,
//                startPoint.y + 23.166f * scale,
//                startPoint.x + 16.184f * scale,
//                startPoint.y + 26.091f * scale,
//                startPoint.x + 12.639f * scale,
//                startPoint.y + 26.091f * scale
//            )
//            cubicTo(
//                startPoint.x + 9.095f * scale,
//                startPoint.y + 26.091f * scale,
//                startPoint.x + 6.228f * scale,
//                startPoint.y + 23.166f * scale,
//                startPoint.x + 6.228f * scale,
//                startPoint.y + 19.557f * scale
//            )
//            cubicTo(
//                startPoint.x + 6.228f * scale,
//                startPoint.y + 16.884f * scale,
//                startPoint.x + 9.745f * scale,
//                startPoint.y + 10.704f * scale,
//                startPoint.x + 11.568f * scale,
//                startPoint.y + 7.677f * scale
//            )
//            close()
//        }
//        drawPath(path = path2, color = greenTint)
//        val path3 = Path().apply {
//            moveTo(startPoint.x + 13f * scale, startPoint.y + 22.47f * scale)
//            cubicTo(
//                startPoint.x + 14.105f * scale,
//                startPoint.y + 22.47f * scale,
//                startPoint.x + 15f * scale,
//                startPoint.y + 21.575f * scale,
//                startPoint.x + 15f * scale,
//                startPoint.y + 20.47f * scale
//            )
//            cubicTo(
//                startPoint.x + 15f * scale,
//                startPoint.y + 19.366f * scale,
//                startPoint.x + 14.105f * scale,
//                startPoint.y + 18.47f * scale,
//                startPoint.x + 13f * scale,
//                startPoint.y + 18.47f * scale
//            )
//            cubicTo(
//                startPoint.x + 11.895f * scale,
//                startPoint.y + 18.47f * scale,
//                startPoint.x + 11f * scale,
//                startPoint.y + 19.366f * scale,
//                startPoint.x + 11f * scale,
//                startPoint.y + 20.47f * scale
//            )
//            cubicTo(
//                startPoint.x + 11f * scale,
//                startPoint.y + 21.575f * scale,
//                startPoint.x + 11.895f * scale,
//                startPoint.y + 22.47f * scale,
//                startPoint.x + 13f * scale,
//                startPoint.y + 22.47f * scale
//            )
//            close()
//        }
//        drawPath(path = path3, color = greenTint)
//    }
//}

@Preview
@Composable
private fun AnimateTempDisplayPreview() {
    QixiaTheme {
        var currentType by remember {
            mutableFloatStateOf(24f)
        }
        LaunchedEffect(Unit) {
            while (true) {
                delay(1000)
                currentType = 16f + (32f - 16f) * Random.nextFloat()
            }
        }
        AnimateTempDisplay(
            modifier = Modifier.fillMaxWidth(1f),
            currentTemperature = currentType
        )
    }
}

@Preview
@Composable
private fun TempAndHumidityDisplayPreview() {
    QixiaTheme {
        TempAndHumidityDisplay(modifier = Modifier.fillMaxWidth())
    }
}