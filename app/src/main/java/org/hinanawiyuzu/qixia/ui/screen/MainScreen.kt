package org.hinanawiyuzu.qixia.ui.screen

import android.graphics.*
import android.net.*
import android.util.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.*
import androidx.navigation.*
import androidx.navigation.compose.*
import kotlinx.coroutines.*
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.*
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.data.source.fake.*
import org.hinanawiyuzu.qixia.ui.*
import org.hinanawiyuzu.qixia.ui.route.*
import org.hinanawiyuzu.qixia.ui.theme.*
import org.hinanawiyuzu.qixia.ui.viewmodel.*
import org.hinanawiyuzu.qixia.utils.*
import java.time.*

private const val BANNER_IMAGE_HEIGHT = 200
private const val CONTENT_PADDING = 15
private const val CARD_HEIGHT = 125
private val fontColor = Color(0xFF59597C)

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    changeBottomBarVisibility: (Boolean) -> Unit,
    viewModel: MainViewModel = viewModel(factory = AppViewModelProvider.factory),
    navController: NavHostController = rememberNavController()
) {
    Log.d("qixia", "别删我Log")
    val fakeTitle = "五月     保心消暑"
    val fakeContent = listOf("春夏之交心阳旺盛", "宜多食清热利湿的食物。")
    val context = LocalContext.current
    val currentLoginUser by viewModel.currentUser.collectAsState()
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MainRoute.MainScreen.name
    ) {
        composable(
            route = MainRoute.MainScreen.name,
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
                    modifier = Modifier
                        .padding(start = CONTENT_PADDING.dp, end = CONTENT_PADDING.dp, top = CONTENT_PADDING.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    if (currentLoginUser.user.isNotEmpty()) {
                        Greeting(
                            sexual = currentLoginUser.user[0].sexual,
                            name = currentLoginUser.user[0].name
                        )
                    }
                    Banner(
                        modifier = Modifier.fillMaxWidth(),
                        images = listOf(
                            R.drawable.banner_example_1.toBitmap(context),
                            R.drawable.banner_example_2.toBitmap(context),
                            R.drawable.banner_example_3.toBitmap(context)
                        )
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "养生日历",
                            color = fontColor,
                            fontSize = FontSize.mediumLargeSize,
                            fontWeight = FontWeight.ExtraBold
                        )
                        HealthCalendar(title = fakeTitle, content = fakeContent) {

                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "亲友提醒",
                            color = fontColor,
                            fontSize = FontSize.mediumLargeSize,
                            fontWeight = FontWeight.ExtraBold
                        )
                        repeat(fakeFamilyRemind.size) {
                            FamilyReminder(familyReminds = fakeFamilyRemind[it])
                        }
                    }
                }
            }
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
private fun Greeting(
    modifier: Modifier = Modifier,
    sexual: String,
    name: String
) {
    Text(
        text = "欢迎  ${name.slice(0..<1)}${if (sexual == "男") "先生" else "女士"}",
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF053B20),
        fontSize = FontSize.loginScreenLoginSize
    )
}

@Composable
private fun Banner(
    modifier: Modifier = Modifier,
    images: List<Bitmap>
) {
    val imageWidthDp = (LocalConfiguration.current.screenWidthDp - CONTENT_PADDING * 2 - 25).dp
    val size = images.size
    var currentFocus by remember { mutableIntStateOf(0) }
    val scrollState = rememberLazyListState()
    LaunchedEffect(currentFocus) {
        scrollState.animateScrollToItem(currentFocus)
    }
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentFocus = (currentFocus + 1) % size
        }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyRow(
                modifier = Modifier
                    .clip(RoundedCornerShape(percent = 10))
                    .fillMaxWidth()
                    .height(BANNER_IMAGE_HEIGHT.dp),
                state = scrollState,
                userScrollEnabled = false
            ) {
                items(size) {
                    Image(
                        modifier = Modifier
                            .width(imageWidthDp)
                            .fillMaxHeight(),
                        bitmap = images[it].asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
            // 要放下面，否则会被覆盖。这就是Box。
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clip(CircleShape)
                    .background(Color(0x11000000)),
                onClick = { currentFocus = if (currentFocus == 0) size - 1 else currentFocus - 1 }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_left_24),
                    contentDescription = "左箭头"
                )
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clip(CircleShape)
                    .background(Color(0x11000000)),
                onClick = { currentFocus = (currentFocus + 1) % size }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
                    contentDescription = "右箭头"
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(size) {
                val animatedColor by animateColorAsState(
                    targetValue = if (it == currentFocus) Color(0xFFB4E3CC) else Color(0xFFD4D4D4),
                    label = "底部小圆球颜色动画"
                )
                Box(
                    modifier = Modifier
                        .animateContentSize()
                        .height(10.dp)
                        .width(if (it == currentFocus) 40.dp else 10.dp)
                        .clip(CircleShape)
                        .background(animatedColor)
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

@Composable
private fun HealthCalendar(
    modifier: Modifier = Modifier,
    title: String,
    content: List<String>,
    onDetailClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 15))
            .fillMaxWidth()
            .height(CARD_HEIGHT.dp)
            .background(brush = Brush.radialGradient(colors = listOf(Color(0xFFFFFFFF), Color(0xFFF4FFFB)))),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(start = 15.dp, top = 5.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier
                    .advancedShadow(
                        color = Color(0xFF7AFE4B),
                        alpha = 0.2f,
                        shadowBlurRadius = 10.dp,
                        cornersRadius = 10.dp,
                        offsetY = 3.dp
                    ),
                text = title,
                color = fontColor,
                fontSize = FontSize.largeSize
            )
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                repeat(content.size) {
                    Text(
                        text = content[it],
                        color = fontColor,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
        GreenArrow(
            modifier = Modifier
                .padding(end = 15.dp)
                .scale(1.5f)
        ) {
            onDetailClicked()
        }
    }
}

@Composable
private fun FamilyReminder(
    modifier: Modifier = Modifier,
    familyReminds: FamilyRemind
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 15))
            .fillMaxWidth()
            .height(CARD_HEIGHT.dp)
            .background(brush = Brush.radialGradient(colors = listOf(Color(0xFFFFFFFF), Color(0xFFF4FFFB)))),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.padding(start = 15.dp, top = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                modifier = Modifier
                    .advancedShadow(
                        color = Color(0xFF7AFE4B),
                        alpha = 0.2f,
                        shadowBlurRadius = 10.dp,
                        cornersRadius = 10.dp,
                        offsetY = 3.dp
                    ),
                text = "您的${familyReminds.relationShip.toChinese()}",
                color = fontColor,
                fontSize = FontSize.largeSize
            )
            Text(
                text = familyReminds.name,
                color = fontColor,
                fontWeight = FontWeight.Light
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 15.dp, bottom = 15.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "在${familyReminds.time.ofChineseIntervalTime(LocalDateTime.now())}",
                color = fontColor,
                fontWeight = FontWeight.Light
            )
            Row {
                Text(
                    text = "提醒您尽快服用",
                    color = fontColor,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = familyReminds.medicineName,
                    color = fontColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}