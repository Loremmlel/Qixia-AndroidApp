package org.hinanawiyuzu.qixia.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.MyIconButton
import org.hinanawiyuzu.qixia.data.source.fake.fakeMedicinesRemindInfo
import org.hinanawiyuzu.qixia.data.source.fake.fakeMedicinesRepoInfo
import org.hinanawiyuzu.qixia.model.MedicineRemindInfo
import org.hinanawiyuzu.qixia.model.MedicineRepoInfo
import org.hinanawiyuzu.qixia.model.TakeMethod
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.theme.MyColor
import org.hinanawiyuzu.qixia.ui.theme.MyColor.greenCardGradient
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.theme.font_deep_green
import org.hinanawiyuzu.qixia.ui.theme.primary_color
import org.hinanawiyuzu.qixia.ui.theme.secondary_color
import org.hinanawiyuzu.qixia.ui.theme.tertiary_color
import org.hinanawiyuzu.qixia.ui.viewmodel.RemindViewModel
import org.hinanawiyuzu.qixia.utils.advancedShadow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun RemindScreen(
    modifier: Modifier = Modifier,
    viewModel: RemindViewModel = viewModel()
) {
    val currentDateTime = LocalDateTime.now()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    Column(
        modifier = modifier
            .verticalScroll(state = rememberScrollState())
            .padding(5.dp)
    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth(),
            onMenuClicked = { /*TODO*/ },
            onAddClicked = {/*TODO*/ }
        )
        Column(
            modifier = Modifier
                .requiredWidth(screenWidth.dp)
                .height(1.dp)
                .background(Color.LightGray)
        ) {}
        Calendar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            currentDateTime = currentDateTime
        )
        TakeMedicineRemind(
            modifier = Modifier
                .padding(5.dp),
            medicinesInfo = fakeMedicinesRemindInfo,
            medicinesImg = listOf(R.drawable.white_pill, R.drawable.pink_pill),
            onDetailClicked = { /*TODO*/ },
            onTakeMedicineClicked = {/*TODO*/ }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MedicinesLeft(
                modifier = Modifier,
                medicinesRepoInfo = fakeMedicinesRepoInfo
            )
            MedicinesExpiry(
                currentDate = currentDateTime.toLocalDate(),
                medicinesRepoInfo = fakeMedicinesRepoInfo
            )
        }
    }
}

/**
 * 顶部导航栏
 * @param modifier 修饰符
 * @param onMenuClicked 左侧的菜单按钮点击事件
 * @param onAddClicked 右上角有个加号，不知道干嘛的
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
                fontSize = FontSize.veryLargeSize,
            )
        )
        MyIconButton(onClick = onAddClicked) {
            Icon(
                painter = painterResource(id = R.drawable.top_bar_add),
                contentDescription = "新增提醒",
                tint = tertiary_color
            )
        }
    }
}

/**
 * 显示日期的部分。
 *
 * 其实还没有完工，例如切换日期看昨天的情况。但是这涉及到数据层，我要怎么操作目前还没头绪 2024-3-30.
 * @param currentDateTime 当前时间
 * @author HinanawiYuzu
 */
@Composable
private fun Calendar(
    modifier: Modifier = Modifier,
    currentDateTime: LocalDateTime
) {
    // 啊，真是奇怪呢。
    val cardWidthDp = LocalConfiguration.current.screenWidthDp * 0.1579
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CalendarItem(
            modifier = Modifier.width(cardWidthDp.dp),
            isSelected = false,
            dateTime = currentDateTime.minusDays(2L)
        )
        CalendarItem(
            modifier = Modifier.width(cardWidthDp.dp),
            isSelected = false,
            dateTime = currentDateTime.minusDays(1L)
        )
        CalendarItem(
            modifier = Modifier.width(cardWidthDp.dp),
            isSelected = true,
            dateTime = currentDateTime
        )
        CalendarItem(
            modifier = Modifier.width(cardWidthDp.dp),
            isSelected = false,
            dateTime = currentDateTime.plusDays(1L)
        )
        CalendarItem(
            modifier = Modifier.width(cardWidthDp.dp),
            isSelected = false,
            dateTime = currentDateTime.plusDays(2L)
        )
    }
}

/**
 * 单个日期卡片。
 * @param isSelected 是否被选择
 * @param dateTime 该卡片显示的时间
 * @author HinanawiYuzu
 */
@Composable
private fun CalendarItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    dateTime: LocalDateTime
) {
    val dayOfWeek = when (dateTime.dayOfWeek!!) {
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
            text = dateTime.dayOfMonth.toString(),
            style = TextStyle(
                color = if (isSelected) Color.White else Color.Gray,
                fontWeight = FontWeight.Black
            )
        )
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
 * @param medicinesInfo 药物信息列表。其类型为自定义的模型类 -> [MedicineRemindInfo]
 * @param medicinesImg 药物图片。虽然目前还是用Drawable但是之后肯定是用本地的资源。
 * @param onDetailClicked 有个绿色箭头，不知道干什么用的。
 * @param onTakeMedicineClicked 卡片的右边有个框框，点击表示自己吃了药。
 * @author HinanawiYuzu
 */
@Composable
private fun TakeMedicineRemind(
    modifier: Modifier = Modifier,
    medicinesInfo: List<MedicineRemindInfo>,
    //TODO: 应当从本地数据库里拉取图片。目前暂时用drawable。
    @DrawableRes medicinesImg: List<Int>,
    onDetailClicked: () -> Unit,
    onTakeMedicineClicked: () -> Unit
) {
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
        repeat(medicinesInfo.size) {
            RemindCard(
                modifier = Modifier
                    .padding(start = 25.dp, end = 15.dp),
                medicineRemindInfo = medicinesInfo[it],
                // 没注意到，引起了越界……
                medicineImg = medicinesImg[it % 2],
                onTakeMedicineClicked = onTakeMedicineClicked
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
 * @param medicineRemindInfo 该卡片要显示的药物提醒信息 -> [MedicineRemindInfo]
 * @param medicineImg 该卡片要显示的图片
 * @param onTakeMedicineClicked 右边按钮点击的事件。
 */
@Composable
private fun RemindCard(
    modifier: Modifier = Modifier,
    medicineRemindInfo: MedicineRemindInfo,
    @DrawableRes medicineImg: Int,
    onTakeMedicineClicked: () -> Unit
) {
    val remindCardHeightDp = LocalConfiguration.current.screenHeightDp * 0.0751
    val method = when (medicineRemindInfo.method) {
        TakeMethod.BeforeMeal -> "饭前"
        TakeMethod.AtMeal -> "饭中"
        TakeMethod.AfterMeal -> "饭后"
        TakeMethod.NotMatter -> "任意"
        TakeMethod.BeforeSleep -> "睡前"
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    id = if (medicineRemindInfo.isTaken) R.drawable.remind_screen_already_taken
                    else R.drawable.remind_screen_not_taken
                ),
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                textAlign = TextAlign.Center,
                text = medicineRemindInfo.time,
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
                Image(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(5.dp),
                    painter = painterResource(id = medicineImg),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .padding(end = 35.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row {
                        Text(
                            text = medicineRemindInfo.name,
                            style = TextStyle(
                                fontWeight = FontWeight.Black,
                                fontSize = FontSize.bigSize
                            )
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            text = medicineRemindInfo.amount,
                            style = TextStyle(
                                color = primary_color,
                                fontWeight = FontWeight.Black,
                                fontSize = FontSize.normalSize
                            )
                        )
                    }
                    Card(
                        modifier = Modifier,
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0x264BFE68)
                        ),
                        shape = RoundedCornerShape(percent = 10)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(0.8f)
                                .fillMaxWidth(0.25f),
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
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 如果已经服用，那么则显示✔。否则显示按钮，用户点击后则变为已服用。
                    if (medicineRemindInfo.isTaken) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(end = 10.dp),
                            painter = painterResource(id = R.drawable.check_circle),
                            contentDescription = "您已于" + medicineRemindInfo.time + "服用" + medicineRemindInfo.name,
                            tint = secondary_color
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                ) { onTakeMedicineClicked() }
                                .align(Alignment.End)
                                .padding(end = 10.dp),
                            painter = painterResource(id = R.drawable.green_circle),
                            contentDescription = "您尚未服用" + medicineRemindInfo.name,
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
 * @param medicinesRepoInfo 药物仓储信息列表 -> [MedicineRepoInfo]
 * @author HinanawiYuzu
 */
@Composable
private fun MedicinesLeft(
    modifier: Modifier = Modifier,
    medicinesRepoInfo: List<MedicineRepoInfo>
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
        medicinesRepoInfo.forEach { medicineRepoInfo ->
            if (((Regex("\\d+").find(medicineRepoInfo.remainAmount))?.value ?: "0").toInt() <= 10) {
                MedicineLeftCard(
                    modifier = Modifier
                        .padding(bottom = 10.dp),
                    medicineRepoInfo = medicineRepoInfo
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
 * @param medicineRepoInfo 该卡片的药物仓储信息 -> [MedicineRepoInfo]
 * @author HinanawiYuzu
 */
@Composable
private fun MedicineLeftCard(
    modifier: Modifier = Modifier,
    medicineRepoInfo: MedicineRepoInfo
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                text = medicineRepoInfo.name,
                style = TextStyle(
                    fontWeight = FontWeight.Black,
                    fontSize = FontSize.tinySize
                )
            )
            Text(
                modifier = Modifier.padding(end = 15.dp),
                text = "需补充!",
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
 * @param currentDate 当前日期
 * @param medicinesRepoInfo 药物仓储信息列表 -> [MedicineRepoInfo]
 * @author HinanawiYuzu
 */
@Composable
private fun MedicinesExpiry(
    modifier: Modifier = Modifier,
    currentDate: LocalDate,
    medicinesRepoInfo: List<MedicineRepoInfo>
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
        medicinesRepoInfo.forEach { medicineRepoInfo ->
            if (medicineRepoInfo.expiryDate <= currentDate) {
                MedicineExpiryCard(
                    modifier = Modifier.padding(bottom = 10.dp),
                    medicineRepoInfo = medicineRepoInfo
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
 * @param medicineRepoInfo 该卡片的药物仓储信息 -> [MedicineRepoInfo]
 * @author HinanawiYuzu
 */
@Composable
private fun MedicineExpiryCard(
    modifier: Modifier = Modifier,
    medicineRepoInfo: MedicineRepoInfo
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                text = medicineRepoInfo.name,
                style = TextStyle(
                    fontWeight = FontWeight.Black,
                    fontSize = FontSize.tinySize
                )
            )
            Text(
                modifier = Modifier.padding(end = 15.dp),
                text = "已过期!",
                style = TextStyle(
                    color = Color(0xFFF50000),
                    fontSize = FontSize.smallSize,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}


@Preview
@Composable
private fun RemindScreenPreview() {
    QixiaTheme {
        RemindScreen()
    }
}