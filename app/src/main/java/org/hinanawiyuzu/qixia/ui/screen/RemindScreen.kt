package org.hinanawiyuzu.qixia.ui.screen

import android.content.*
import android.graphics.*
import android.net.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
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
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.*
import androidx.navigation.*
import androidx.navigation.compose.*
import kotlinx.coroutines.*
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.*
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.ui.*
import org.hinanawiyuzu.qixia.ui.theme.*
import org.hinanawiyuzu.qixia.ui.theme.MyColor.greenCardGradient
import org.hinanawiyuzu.qixia.ui.viewmodel.*
import org.hinanawiyuzu.qixia.ui.viewmodel.shared.*
import org.hinanawiyuzu.qixia.utils.*
import java.time.*
import kotlin.reflect.*

var searchImages: KFunction1<MedicineRemind, Uri>? = null
val currentDate: LocalDate = LocalDate.now()

/**
 * 主页之提醒页面
 * @param modifier 修饰符
 * @param changeBottomBarVisibility
 * 用来改变底部导航栏的可见性。因为这个页面涉及到导航至其它页面的操作，因为我AppScreen的设计缺陷，所以需要专门的函数管理底部导航栏的可见性。
 * @param sharedViewModel 用来传递数据的ViewModel。这个ViewModel是用来传递数据的，因为我目前没发现popBackStack()可以传递数据。
 * @param viewModel 用来管理数据的ViewModel
 * @param navController 用来管理导航的NavController
 * @see RemindViewModel
 * @see SharedBetweenMedicineRepoAndNewRemindViewModel
 * @author HinanawiYuzu
 */
@Composable
fun RemindScreen(
    modifier: Modifier = Modifier,
    changeBottomBarVisibility: (Boolean) -> Unit,
    // 用sharedViewModel来传递数据也是不得已而为之。因为我目前没发现popBackStack()可以传递数据。
    sharedViewModel: SharedBetweenMedicineRepoAndNewRemindViewModel = viewModel(),
    viewModel: RemindViewModel = viewModel(factory = AppViewModelProvider.factory),
    navController: NavHostController = rememberNavController()
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val allMedicineRemind by viewModel.allMedicineRemind.collectAsState()
    val allMedicineRepo by viewModel.allMedicineRepo.collectAsState()
    searchImages = viewModel::searchImageFromMedicineRepo
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = RemindRoute.RemindScreen.name
    ) {
        composable(
            route = RemindRoute.RemindScreen.name,
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
                    .padding(5.dp)
            ) {
                TopBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onMenuClicked = { /*TODO*/ },
                    onAddClicked = { navController.navigate(RemindRoute.NewRemindScreen.name) }
                )
                GrayLine(screenWidthDp = screenWidthDp)
                Calendar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    currentSelectedDate = viewModel.currentSelectedDate,
                    onCalendarClicked = viewModel::onCalendarClicked
                )
                Column(
                    modifier = Modifier
                        .verticalScroll(state = rememberScrollState())
                ) {
                    TakeMedicineRemind(
                        modifier = Modifier
                            .padding(5.dp),
                        currentSelectedDate = viewModel.currentSelectedDate,
                        medicineReminds = allMedicineRemind.medicineRemindList,
                        onDetailClicked = { /*TODO*/ },
                        onTakeMedicineClicked = viewModel::onTakeMedicineClicked,
                        onImageClicked = { navController.navigate("${RemindRoute.ImageDetailScreen.name}/$it") }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        MedicinesLeft(
                            modifier = Modifier.weight(1f),
                            medicineRepos = allMedicineRepo.allMedicineRepoList
                        )
                        Spacer(modifier = Modifier.weight(0.05f))
                        MedicinesExpiry(
                            modifier = Modifier.weight(1f),
                            currentSelectedDate = viewModel.currentSelectedDate,
                            medicinesRepos = allMedicineRepo.allMedicineRepoList
                        )
                    }
                }
            }
        }
        slideComposable(route = RemindRoute.NewRemindScreen.name)
        {
            changeBottomBarVisibility(false)
            NewRemindScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
        slideComposable(route = RemindRoute.MedicineRepoScreen.name) {
            changeBottomBarVisibility(false)
            MedicineRepoScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
        slideComposable(route = RemindRoute.NewMedicineScreen.name) {
            changeBottomBarVisibility(false)
            NewMedicineScreen(
                navController = navController
            )
        }
        imageDetailComposable(
            route = "${RemindRoute.ImageDetailScreen.name}/{uri}",
            arguments = listOf(navArgument("uri") { type = NavType.StringType })
        ) {
            changeBottomBarVisibility(false)
            FullScreenImageView(
                backStackEntry = it,
                onDismiss = { navController.popBackStack() }
            )
        }
    }
}

/**
 * 顶部导航栏
 * @param modifier 修饰符
 * @param onMenuClicked 左侧的菜单按钮点击事件
 * @param onAddClicked 右上角有个加号,点击后进入新增提醒界面
 * @author HinanawiYuzu
 */
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    onMenuClicked: () -> Unit,
    onAddClicked: () -> Unit
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
            text = "提醒",
            style = TextStyle(
                fontSize = FontSize.mediumLargeSize,
            )
        )
        MyIconButton(onClick = onAddClicked) {
            Icon(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.top_bar_add),
                contentDescription = "新增提醒",
                tint = tertiary_color
            )
        }
    }
}

/**
 * 显示日期的部分。
 * @param modifier 修饰符
 * @param currentSelectedDate 当前选择的时间
 * @param onCalendarClicked 点击日期的事件，会更改[RemindViewModel.currentSelectedDate]
 * @author HinanawiYuzu
 */
@Composable
private fun Calendar(
    modifier: Modifier = Modifier,
    currentSelectedDate: LocalDate,
    onCalendarClicked: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val currentDate = LocalDate.now()
    // 啊，真是奇怪呢。
    val cardWidthDp = LocalConfiguration.current.screenWidthDp * 0.1579
    LaunchedEffect(key1 = true) {
        listState.scrollToItem(13)
    }
    LazyRow(
        modifier = modifier,
        state = listState,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(30) { index ->
            val calendarDate = currentDate.plusDays((index - 15).toLong())
            CalendarItem(
                modifier = Modifier.width(cardWidthDp.dp),
                isSelected = calendarDate == currentSelectedDate,
                date = calendarDate,
                onClicked = { onCalendarClicked(index) }
            )
        }
    }
}

/**
 * 单个日期卡片。
 * @param modifier 修饰符
 * @param isSelected 是否被选择
 * @param date 该卡片显示的时间
 * @param onClicked 点击事件，和[Calendar]的onCalendarClicked对应。
 * @author HinanawiYuzu
 */
@Composable
private fun CalendarItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    date: LocalDate,
    onClicked: () -> Unit
) {
    val dayOfWeek = when (date.dayOfWeek!!) {
        DayOfWeek.MONDAY -> "周一"
        DayOfWeek.TUESDAY -> "周二"
        DayOfWeek.WEDNESDAY -> "周三"
        DayOfWeek.THURSDAY -> "周四"
        DayOfWeek.FRIDAY -> "周五"
        DayOfWeek.SATURDAY -> "周六"
        DayOfWeek.SUNDAY -> "周日"
    }
    Column(
        modifier = modifier
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) { onClicked() }
            .clip(RoundedCornerShape(percent = 40))
            .aspectRatio(0.77f)
            .border(
                width = 2.dp,
                brush = if (isSelected) MyColor.selectedCalendarCardBoardGradient
                else MyColor.notSelectedCalendarCardBoardGradient,
                shape = RoundedCornerShape(percent = 40)
            )
            .background(
                brush = if (isSelected) MyColor.selectedCalendarCardContainerGradient
                else Brush.linearGradient(colors = listOf(Color.White, Color.White))
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "${date.monthValue}.${date.dayOfMonth}",
            style = TextStyle(
                color = if (isSelected) Color.White else Color.Gray,
                fontWeight = FontWeight.Black
            )
        )
        if (date == currentDate) {
            Text(
                text = "今天",
                style = TextStyle(
                    color = if (isSelected) Color.White else Color.Gray,
                    fontWeight = FontWeight.Black
                )
            )
        }
        Text(
            text = dayOfWeek,
            style = TextStyle(
                color = if (isSelected) Color.White else Color.Gray,
                fontWeight = FontWeight.Black
            )
        )
    }
}

/**
 * 本页面主体部分，提醒用户吃药的卡片。
 * @param modifier 修饰符
 * @param currentSelectedDate 当前选择的日期
 * @param medicineReminds 药物信息列表。其类型为自定义的模型类 -> [MedicineRemind]
 * @param onDetailClicked 有个绿色箭头，不知道干什么用的。
 * @param onTakeMedicineClicked 卡片的右边有个框框，点击表示自己吃了药。使用的函数是[RemindViewModel.onTakeMedicineClicked]
 * @param onImageClicked 点击药物图片的事件，会跳转到图片详情页面。这个函数的参数是图片的Uri。
 * @author HinanawiYuzu
 */
@Composable
private fun TakeMedicineRemind(
    modifier: Modifier = Modifier,
    currentSelectedDate: LocalDate,
    medicineReminds: List<MedicineRemind>,
    onDetailClicked: () -> Unit,
    onTakeMedicineClicked: (Int, Context) -> Unit,
    onImageClicked: (String) -> Unit
) {
    val context = LocalContext.current
    val displayedMedicineReminds = medicineReminds
        .filter {
            currentSelectedDate in it.startDate..it.endDate && it.isDisplayedInLocalDate(currentSelectedDate)
        }
        .sortedBy { it.remindTime }
    val displayedImagesUri = displayedMedicineReminds.map { searchImages!!.invoke(it) }
    var displayedImages by remember { mutableStateOf(List<Bitmap?>(0) { null }) }
    // 图片的解码是非常耗时的工作！
    // 所以我用了一个协程来解决这个问题。
    // 如果不使用，那么会造成明显的卡顿！！！
    // 在加载完毕之前，显示的是一个大小为1*1的位图。
    // 啊，这种感觉是多么美妙~~😋
    LaunchedEffect(displayedImagesUri) {
//        withContext(Dispatchers.IO) {
//            displayedImages = displayedImagesUri.map {
//                it.let { uri ->
//                    BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
//                }
//            }
//        }
        displayedImages = coroutineScope {
            displayedImagesUri.map { uri ->
                async(Dispatchers.IO) {
                    BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
                }
            }.awaitAll()
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            modifier = Modifier.align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 5.dp),
                text = "服药提醒",
                style = TextStyle(
                    color = font_deep_green,
                    fontSize = FontSize.largeSize
                )
            )
            GreenArrow(onClicked = {/*TODO*/ })
        }
        if (displayedMedicineReminds.isEmpty()) {
            Text(
                text = "没有服药提醒,点击右上角+号添加",
                style = TextStyle(
                    color = font_deep_green
                )
            )
            return
        }
        repeat(displayedMedicineReminds.size) { index ->
            RemindCard(
                modifier = Modifier
                    .padding(start = 25.dp, end = 15.dp),
                currentSelectedDate = currentSelectedDate,
                medicineRemind = displayedMedicineReminds[index],
                // 如果不检查下标是否溢出的话，还是会发生数组越界异常。
                // 原因在于用户快速切换日期时显示的图片数组大小会变化，而更新又是在后台做的。
                medicineImg = if (displayedImages.isNotEmpty() && index < displayedImages.size) displayedImages[index]
                else null,
                onTakeMedicineClicked = { onTakeMedicineClicked(displayedMedicineReminds[index].id, context) },
                onImageClicked = { onImageClicked(displayedImagesUri[index].toString().replace("/", "*")) }
            )
        }
    }
}

/**
 * 这就是上面提到的绿色箭头。下面还会有两个模块要用到这玩意儿。
 * @param modifier 修饰符
 * @param onClicked 点击事件
 * @author HinanawiYuzu
 */
@Composable
private fun GreenArrow(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    MyIconButton(
        modifier = modifier,
        onClick = onClicked
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .advancedShadow(
                        color = primary_color,
                        alpha = 0.2f,
                        shadowBlurRadius = 5.dp,
                        cornersRadius = 10.dp,
                        offsetY = 5.dp
                    ),
                painter = painterResource(id = R.drawable.remind_screen_rec_back),
                contentDescription = null
            )
            Image(
                painter = painterResource(id = R.drawable.remind_screen_rec_arrow),
                contentDescription = null
            )
        }
    }
}

/**
 * 单个提醒卡片。
 *
 * BYD这个函数嵌套真多啊。
 * @param modifier 修饰符。
 * @param currentSelectedDate 当前选择的日期
 * @param medicineRemind 该卡片要显示的药物提醒信息 -> [MedicineRemind]
 * @param medicineImg 该卡片要显示的图片
 * @param onTakeMedicineClicked 右边按钮点击的事件。和[TakeMedicineRemind]的onTakeMedicineClicked对应。
 * @param onImageClicked 点击药物图片的事件，会跳转到图片详情页面。这个函数的参数是图片的Uri。和[TakeMedicineRemind]的onImageClicked对应。
 * @author HinanawiYuzu
 */
@Composable
private fun RemindCard(
    modifier: Modifier = Modifier,
    currentSelectedDate: LocalDate,
    medicineRemind: MedicineRemind,
    medicineImg: Bitmap?,
    onTakeMedicineClicked: () -> Unit,
    onImageClicked: () -> Unit
) {
    val remindCardHeightDp = LocalConfiguration.current.screenHeightDp * 0.085
    val method = medicineRemind.method.convertToDisplayedString()
    val checked =
        medicineRemind.isTaken[medicineRemind.startDate.numberOfMedicineTakenUntilNow(medicineRemind.frequency) - 1]
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    id = if (checked) R.drawable.remind_screen_already_taken
                    else R.drawable.remind_screen_not_taken
                ),
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                textAlign = TextAlign.Center,
                text = LocalTimeConverter.toDisplayString(medicineRemind.remindTime),
                style = TextStyle(
                    color = MyColor.font_deep_blue,
                    fontSize = FontSize.largeSize,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
        Column(
            modifier = Modifier
                .height(remindCardHeightDp.dp)
                .clip(RoundedCornerShape(percent = 30))
                .fillMaxWidth()
                .background(brush = greenCardGradient)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 药物的图片
                Image(
                    modifier = Modifier
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                        ) { onImageClicked.invoke() }
                        .fillMaxWidth(0.15f)
                        .fillMaxHeight(0.9f)
                        .padding(5.dp),
                    bitmap = medicineImg?.asImageBitmap() ?: ImageBitmap(1, 1),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(end = 35.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // 药物的名字和剂量
                            Text(
                                modifier = Modifier.fillMaxWidth(0.7f),
                                text = medicineRemind.name,
                                style = TextStyle(
                                    fontWeight = FontWeight.Black,
                                    fontSize = FontSize.bigSize
                                )
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(
                                text = "剂量:${medicineRemind.dose}",
                                style = TextStyle(
                                    color = primary_color,
                                    fontWeight = FontWeight.Black,
                                    fontSize = FontSize.smallSize
                                )
                            )
                        }
                        // 药物的服用方式、注意事项
                        Card(
                            modifier = Modifier
                                .height(25.dp)
                                .width(50.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0x264BFE68)
                            ),
                            shape = RoundedCornerShape(percent = 20)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = method,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Black,
                                        color = primary_color,
                                        fontSize = FontSize.normalSize
                                    )
                                )
                            }
                        }
                    }
                    // 如果已经服用，那么则显示✔。否则显示按钮，用户点击后则变为已服用。
                    if (!checked) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(10.dp)
                                .fillMaxWidth(0.15f)
                                .fillMaxHeight(0.7f)
                                .then(
                                    if (currentSelectedDate == LocalDate.now()) {
                                        Modifier.clickable(
                                            interactionSource = MutableInteractionSource(),
                                            indication = null
                                        ) { onTakeMedicineClicked() }
                                    } else Modifier
                                ),
                            painter = painterResource(id = R.drawable.green_circle),
                            contentDescription = "您尚未服用" + medicineRemind.name,
                            tint = secondary_color
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 10.dp)
                                .fillMaxWidth(0.15f)
                                .fillMaxHeight(0.7f),
                            painter = painterResource(id = R.drawable.check_circle),
                            contentDescription = "您已于" + medicineRemind.remindTime + "服用" + medicineRemind.name,
                            tint = secondary_color
                        )
                    }
                }
            }
        }
    }
}

/**
 * 提醒用户有没有药物过期的部分。
 * @param modifier 修饰符
 * @param medicineRepos 药物仓储信息列表 -> [MedicineRepo]
 * @author HinanawiYuzu
 */
@Composable
private fun MedicinesLeft(
    modifier: Modifier = Modifier,
    medicineRepos: List<MedicineRepo>
) {
    // 标识是否有任何药物库存不足
    var isAnyMedicineNotEnough = false
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "药品余量",
                style = TextStyle(
                    color = font_deep_green,
                    fontSize = FontSize.largeSize
                )
            )
            GreenArrow(onClicked = {/*TODO*/ })
        }
        // 如果有库存不足的药物，再显示。
        medicineRepos.forEach { medicineRepoInfo ->
            if (((Regex("\\d+").find(medicineRepoInfo.remainAmount))?.value ?: "0").toInt() <= 10) {
                MedicineLeftCard(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                    medicineRepo = medicineRepoInfo
                )
                isAnyMedicineNotEnough = true
            }
        }
        if (!isAnyMedicineNotEnough) {
            Text(
                text = "药物余量充足",
                style = TextStyle(
                    color = font_deep_green
                )
            )
        }
    }
}

/**
 * 单个显示药物库存信息的卡片。
 * @param modifier 修饰符
 * @param medicineRepo 该卡片的药物仓储信息 -> [MedicineRepo]
 * @author HinanawiYuzu
 */
@Composable
private fun MedicineLeftCard(
    modifier: Modifier = Modifier,
    medicineRepo: MedicineRepo
) {
    val leftCardHeightDp = LocalConfiguration.current.screenHeightDp * 0.0396
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 40))
            .background(brush = MyColor.yellowCardGradient)
            .height(leftCardHeightDp.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                text = medicineRepo.name,
                style = TextStyle(
                    fontWeight = FontWeight.Black,
                    fontSize = FontSize.tinySize
                )
            )
            Text(
                modifier = Modifier.padding(end = 15.dp),
                text = "需补充",
                style = TextStyle(
                    color = Color(0xFFF59A19),
                    fontSize = FontSize.smallSize,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

/**
 * 显示药物有无过期的部分。
 * @param modifier 修饰符
 * @param currentSelectedDate 当前日期
 * @param medicinesRepos 药物仓储信息列表 -> [MedicineRepo]
 * @author HinanawiYuzu
 */
@Composable
private fun MedicinesExpiry(
    modifier: Modifier = Modifier,
    currentSelectedDate: LocalDate,
    medicinesRepos: List<MedicineRepo>
) {
    // 标识是否有任何药物过期
    var isAnyMedicineOutOfDate = false
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "药品过期",
                style = TextStyle(
                    color = font_deep_green,
                    fontSize = FontSize.largeSize
                )
            )
            GreenArrow(onClicked = {})
        }
        medicinesRepos.forEach { medicineRepoInfo ->
            if (medicineRepoInfo.expiryDate < currentSelectedDate) {
                MedicineExpiryCard(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                    medicineRepo = medicineRepoInfo
                )
                isAnyMedicineOutOfDate = true
            }
        }
        if (!isAnyMedicineOutOfDate) {
            Text(
                text = "没有药品过期",
                style = TextStyle(
                    color = font_deep_green
                )
            )
        }
    }
}

/**
 *显示药物过期信息的卡片
 * @param modifier 修饰符
 * @param medicineRepo 该卡片的药物仓储信息 -> [MedicineRepo]
 * @author HinanawiYuzu
 */
@Composable
private fun MedicineExpiryCard(
    modifier: Modifier = Modifier,
    medicineRepo: MedicineRepo
) {
    val expiryCardHeightDp = LocalConfiguration.current.screenHeightDp * 0.0396
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 40))
            .background(brush = MyColor.redCardGradient)
            .height(expiryCardHeightDp.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                text = medicineRepo.name,
                style = TextStyle(
                    fontWeight = FontWeight.Black,
                    fontSize = FontSize.tinySize
                )
            )
            Text(
                modifier = Modifier.padding(end = 15.dp),
                text = "已过期",
                style = TextStyle(
                    color = Color(0xFFF50000),
                    fontSize = FontSize.smallSize,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}