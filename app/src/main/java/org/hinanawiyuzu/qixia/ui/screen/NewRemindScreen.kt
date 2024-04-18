package org.hinanawiyuzu.qixia.ui.screen

import android.app.*
import android.content.*
import android.os.*
import android.provider.*
import androidx.activity.compose.*
import androidx.activity.result.contract.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.*
import androidx.navigation.*
import androidx.navigation.compose.*
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.*
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.ui.*
import org.hinanawiyuzu.qixia.ui.theme.*
import org.hinanawiyuzu.qixia.ui.theme.MyColor.transparentButtonBorderGradient
import org.hinanawiyuzu.qixia.ui.viewmodel.*
import org.hinanawiyuzu.qixia.ui.viewmodel.shared.*
import org.hinanawiyuzu.qixia.utils.*
import java.time.*

private enum class LoadState {
    Loading,
    Success,
    Error
}


/**
 * 新增提醒页面
 * @param modifier 修饰符
 * @param sharedViewModel 用于共享数据的ViewModel
 * @param viewModel 用于处理逻辑的ViewModel
 * @param navController 导航控制器
 * @see SharedBetweenMedicineRepoAndNewRemindViewModel
 * @see NewRemindViewModel
 * @author HinanawiYuzu
 */
@Composable
fun NewRemindScreen(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedBetweenMedicineRepoAndNewRemindViewModel,
    viewModel: NewRemindViewModel = viewModel(factory = AppViewModelProvider.factory),
    navController: NavHostController = rememberNavController(),
) {
    val selectorHeight: Dp = 35.dp
    val screenWidthDp: Dp = LocalConfiguration.current.screenWidthDp.dp
    val context = LocalContext.current
    viewModel.medicineRepoId = sharedViewModel.medicineRepoId
    sharedViewModel.medicineRepoId?.let {
        viewModel.getMedicineRepo()
    }
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
                    .padding(10.dp)
                    .verticalScroll(
                        state = rememberScrollState(),
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                Column {
                    Text(
                        text = "药品名称",
                        style = TextStyle(
                            fontSize = FontSize.bigSize
                        )
                    )
                    MedicineSelector(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(selectorHeight),
                        medicineName = viewModel.medicineName,
                        onSelectMedicineFromBoxClicked = {
                            viewModel.onSelectMedicineFromBoxClicked(navController)
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "单次服用剂量",
                            style = TextStyle(
                                fontSize = FontSize.bigSize
                            )
                        )
                        DoseSelector(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(selectorHeight),
                            dose = viewModel.dose,
                            onDropDownMenuItemClicked = viewModel::onDoseDropDownMenuItemClicked
                        )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "服用频率",
                            style = TextStyle(
                                fontSize = FontSize.bigSize
                            )
                        )
                        FrequencySelector(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(selectorHeight),
                            frequency = viewModel.frequency,
                            onDropDownMenuItemClicked = viewModel::onFrequencyDropDownMenuItemClicked
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "提醒开始日期",
                            style = TextStyle(
                                fontSize = FontSize.bigSize
                            )
                        )
                        StartDateSelector(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(selectorHeight),
                            startDate = viewModel.startDate,
                            onStartDatePickerConfirmButtonClicked = viewModel::onStartDatePickerConfirmButtonClicked
                        )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "提醒结束日期",
                            style = TextStyle(
                                fontSize = FontSize.bigSize
                            )
                        )
                        EndDateSelector(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(selectorHeight),
                            startDate = viewModel.startDate,
                            endDate = viewModel.endDate,
                            onEndDatePickerConfirmButtonClicked = viewModel::onEndDatePickerConfirmButtonClicked
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.Start)
                ) {
                    Text(
                        text = "服药提醒时间",
                        style = TextStyle(
                            fontSize = FontSize.bigSize
                        )
                    )
                    RemindTimeSelector(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(selectorHeight),
                        onRemindTimeSelected = viewModel::onRemindTimeSelected
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "服药&饭点",
                        style = TextStyle(
                            fontSize = FontSize.bigSize
                        )
                    )
                    MethodSelector(
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                            .height(screenWidthDp / 4),
                        selectedMethod = viewModel.method,
                        onSelectMethodClicked = viewModel::onSelectMethodClicked
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        CommitButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(15.dp)
                .fillMaxWidth(),
            buttonHeight = 70.dp,
            enabled = viewModel.buttonEnabled,
            onNextClicked = { viewModel.onCommitButtonClicked(context, navController, it) }
        )
    }
}

/**
 * 顶部栏
 * @param modifier 修饰符
 * @param onBackClicked 返回按钮点击事件
 * @author HinanawiYuzu
 */
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
            text = "新增提醒",
            style = TextStyle(
                fontSize = FontSize.veryLargeSize,
            )
        )
        Spacer(modifier = Modifier.size(40.dp))
    }
}

/**
 * 选择药品名称的卡片
 * @param modifier 修饰符
 * @param medicineName 药品名称
 * @param onSelectMedicineFromBoxClicked 从药箱中选择药品的点击事件, 执行的函数是[NewRemindViewModel.onSelectMedicineFromBoxClicked]
 * @author HinanawiYuzu
 */
@Composable
private fun MedicineSelector(
    modifier: Modifier = Modifier,
    medicineName: String,
    onSelectMedicineFromBoxClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 40))
            .background(brush = MyColor.greenCardGradient),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier
                .padding(start = 10.dp, top = 3.dp, bottom = 3.dp),
            painter = painterResource(id = R.drawable.new_remind_screen_add_medicine),
            contentDescription = "新增药品"
        )
        Text(
            text = medicineName
        )
        Row(
            modifier = Modifier
                .clickable { onSelectMedicineFromBoxClicked() }
                .padding(end = 10.dp, top = 3.dp, bottom = 3.dp)
                .fillMaxHeight(0.8f)
                .width(120.dp)
                .clip(RoundedCornerShape(percent = 40))
                .background(brush = MyColor.deepGreenButtonGradient),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "从药箱中选择"
            )
        }
    }
}

/**
 * 选择剂量的卡片
 * @param modifier 修饰符
 * @param onDropDownMenuItemClicked 下拉菜单中的选项点击事件, 执行的函数是[NewRemindViewModel.onDoseDropDownMenuItemClicked]
 * @param dose 剂量, 例如"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"等等
 * @author HinanawiYuzu
 */
@Composable
private fun DoseSelector(
    modifier: Modifier = Modifier,
    onDropDownMenuItemClicked: (String) -> Unit,
    dose: String?
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
    Box {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(percent = 40))
                .background(brush = MyColor.greenCardGradient),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .padding(start = 10.dp, top = 3.dp, bottom = 3.dp),
                painter = painterResource(id = R.drawable.new_remind_screen_leading_icon),
                contentDescription = "单次服用剂量"
            )
            Text(text = dose ?: " ")
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "片",
                    style = TextStyle(fontSize = FontSize.bigSize)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        painter = painterResource(id = R.drawable.green_triangle),
                        contentDescription = "选择单次服用剂量",
                        tint = secondary_color
                    )
                }
            }
        }
        DropdownMenu(
            modifier = Modifier.align(Alignment.BottomCenter),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onDropDownMenuItemClicked(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

/**
 * 选择服用频率的卡片
 * @param modifier 修饰符
 * @param frequency 服用频率, 请参阅[MedicineFrequency]
 * @param onDropDownMenuItemClicked 下拉菜单中的选项点击事件, 执行的函数是[NewRemindViewModel.onFrequencyDropDownMenuItemClicked]
 * @author HinanawiYuzu
 */
@Composable
private fun FrequencySelector(
    modifier: Modifier = Modifier,
    frequency: MedicineFrequency?,
    onDropDownMenuItemClicked: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = MedicineFrequency.entries.map { it.convertToString() }
    Box {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(percent = 40))
                .background(brush = MyColor.greenCardGradient),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .padding(start = 10.dp, top = 3.dp, bottom = 3.dp),
                painter = painterResource(id = R.drawable.new_remind_screen_leading_icon),
                contentDescription = "服用频率"
            )
            Text(
                text = frequency?.convertToString() ?: "",
                style = TextStyle(fontSize = FontSize.bigSize)
            )
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    painter = painterResource(id = R.drawable.green_triangle),
                    contentDescription = "选择服用频率",
                    tint = secondary_color
                )
            }
        }
        DropdownMenu(
            modifier = Modifier.align(Alignment.BottomCenter),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onDropDownMenuItemClicked(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * 选择开始日期的卡片
 * @param modifier 修饰符
 * @param startDate 开始日期，和[NewRemindViewModel.startDate]对应
 * @param onStartDatePickerConfirmButtonClicked 开始日期选择器中的确定按钮点击事件, 执行的函数是[NewRemindViewModel.onStartDatePickerConfirmButtonClicked]
 * @author HinanawiYuzu
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StartDateSelector(
    modifier: Modifier = Modifier,
    startDate: LocalDate?,
    onStartDatePickerConfirmButtonClicked: (Long?) -> Unit
) {
    var isDatePickerDialogOpen by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    Row(
        modifier = modifier
            .clickable {
                isDatePickerDialogOpen = !isDatePickerDialogOpen
            }
            .clip(RoundedCornerShape(percent = 40))
            .background(brush = MyColor.greenCardGradient),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.padding(start = 10.dp),
            painter = painterResource(id = R.drawable.calendar),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(end = 10.dp),
            text = "${startDate?.year ?: "    "}年${startDate?.monthValue ?: "  "}月${startDate?.dayOfMonth ?: "  "}日",
            style = TextStyle(fontSize = FontSize.normalSize)
        )
    }
    if (isDatePickerDialogOpen) {
        DatePickerDialog(
            onDismissRequest = { isDatePickerDialogOpen = false },
            confirmButton = {
                Button(onClick = {
                    isDatePickerDialogOpen = false
                    val selectedTimeMillis = datePickerState.selectedDateMillis
                    onStartDatePickerConfirmButtonClicked(selectedTimeMillis)
                }) {
                    Text(
                        modifier = Modifier,
                        text = "确定",
                        style = TextStyle(
                            fontSize = FontSize.bigSize,
                            color = Color.White
                        )
                    )
                }
            },
        ) {
            DatePicker(
                state = datePickerState,
                dateValidator = { date ->
                    // 如果日期时间戳小于当前时间，则不可用。不能选择。
                    // 86400000 是一天的毫秒数，减去表示可选今天。
                    return@DatePicker date >= Instant.now().toEpochMilli() - 86400000
                },
            )
        }
    }
}

/**
 * 选择结束日期的卡片
 *
 * 我仔细想了下，发现还是分开来，逻辑会简单好处理一些。
 *
 * 主要的逻辑是：
 * 不能选择比开始日期更早的结束日期。
 *
 * 在已经选择结束日期的情况下，再选择开始日期，如果选择的开始日期大于已选择的结束日期，那么结束日期清零。
 * 第二个逻辑位于[NewRemindViewModel.onStartDatePickerConfirmButtonClicked]中。
 * @param modifier 修饰符
 * @param startDate 开始日期，和[NewRemindViewModel.startDate]对应
 * @param endDate 结束日期，和[NewRemindViewModel.endDate]对应
 * @param onEndDatePickerConfirmButtonClicked 结束日期选择器中的确定按钮点击事件, 执行的函数是[NewRemindViewModel.onEndDatePickerConfirmButtonClicked]
 * @author HinanawiYuzu
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EndDateSelector(
    modifier: Modifier = Modifier,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onEndDatePickerConfirmButtonClicked: (Long?) -> Unit
) {
    var isDatePickerDialogOpen by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    Row(
        modifier = modifier
            .clickable {
                isDatePickerDialogOpen = !isDatePickerDialogOpen
            }
            .clip(RoundedCornerShape(percent = 40))
            .background(brush = MyColor.greenCardGradient),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.padding(start = 10.dp),
            painter = painterResource(id = R.drawable.calendar),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(end = 10.dp),
            text = "${endDate?.year ?: "    "}年${endDate?.monthValue ?: "  "}月${endDate?.dayOfMonth ?: "  "}日",
            style = TextStyle(fontSize = FontSize.normalSize)
        )
    }
    if (isDatePickerDialogOpen) {
        DatePickerDialog(
            onDismissRequest = { isDatePickerDialogOpen = false },
            confirmButton = {
                Button(onClick = {
                    isDatePickerDialogOpen = false
                    val selectedTimeMillis = datePickerState.selectedDateMillis
                    onEndDatePickerConfirmButtonClicked(selectedTimeMillis)
                }) {
                    Text(
                        modifier = Modifier,
                        text = "确定",
                        style = TextStyle(
                            fontSize = FontSize.bigSize,
                            color = Color.White
                        )
                    )
                }
            },
        ) {
            DatePicker(
                state = datePickerState,
                dateValidator = { date ->
                    // 如果日期时间戳小于当前时间，则不可用。不能选择。
                    // 86400000 是一天的毫秒数，减去表示可选今天。
                    return@DatePicker ((date >= Instant.now().toEpochMilli() - 86400000
                            && date >= (startDate
                        ?.atStartOfDay(ZoneId.systemDefault())
                        ?.toInstant()
                        ?.toEpochMilli()
                        ?: (Instant.now().toEpochMilli() - 86400000))))
                },
            )
        }
    }
}

/**
 * 选择提醒时间的卡片
 * @param modifier 修饰符
 * @param onRemindTimeSelected 提醒时间选择事件，返回的是小时和分钟，执行的函数是[NewRemindViewModel.onRemindTimeSelected]
 * @see TimePickerDialog
 * @author HinanawiYuzu
 */
@Composable
private fun RemindTimeSelector(
    modifier: Modifier = Modifier,
    onRemindTimeSelected: (Int, Int) -> Unit
) {
    val hour = remember { mutableIntStateOf(0) }
    val minute = remember { mutableIntStateOf(0) }
    var visible by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .clickable {
                visible = true
            }
            .clip(RoundedCornerShape(percent = 40))
            .background(brush = MyColor.greenCardGradient),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            modifier = Modifier.padding(start = 10.dp),
            painter = painterResource(id = R.drawable.bell),
            contentDescription = "选择提醒时间"
        )
        Text(
            modifier = Modifier.padding(start = 20.dp),
            text = "${hour.intValue.toString().padStart(2, '0')}:${
                minute.intValue.toString().padStart(2, '0')
            }",
            style = TextStyle(
                fontSize = FontSize.bigSize,
                fontWeight = FontWeight.Bold
            )
        )
    }
    TimePickerDialog(
        visible = visible,
        onDismissRequest = {
            visible = false
            onRemindTimeSelected(hour.intValue, minute.intValue)
        },
        hour = hour,
        minute = minute
    )
}

// 明明之前的页面有任意的选项，但是这里却只有饭前、饭中和饭后。自相矛盾的UI设计，完全没考虑全面啊。
/**
 * 服药方法的选择器
 * @param modifier 修饰符
 * @param selectedMethod 选择的方法，请参阅[TakeMethod]
 * @param onSelectMethodClicked 选择方法的点击事件，返回的是方法的Int值，执行的函数是[NewRemindViewModel.onSelectMethodClicked]
 * @author HinanawiYuzu
 */
@Composable
private fun MethodSelector(
    modifier: Modifier = Modifier,
    selectedMethod: TakeMethod?,
    onSelectMethodClicked: (Int) -> Unit
) {
    val animationSpec = 300
    val selected: Int? = when (selectedMethod) {
        TakeMethod.BeforeMeal -> 0
        TakeMethod.AtMeal -> 1
        TakeMethod.AfterMeal -> 2
        else -> null
    }
    val beforeMealBackgroundTint by animateColorAsState(
        targetValue = if (selected == 0) Color(0xFF1BD15D) else Color.Unspecified,
        animationSpec = tween(animationSpec),
        label = "beforeMealBackgroundTint"
    )
    val beforeMealContentTint by animateColorAsState(
        targetValue = if (selected == 0) Color.White else Color(0xFF9B9B9B),
        animationSpec = tween(animationSpec),
        label = "beforeMealContentTint"
    )
    val atMealBackgroundTint by animateColorAsState(
        targetValue = if (selected == 1) Color(0xFF1BD15D) else Color.Unspecified,
        animationSpec = tween(animationSpec),
        label = "atMealBackgroundTint"
    )
    val atMealContentTint by animateColorAsState(
        targetValue = if (selected == 1) Color.White else Color(0xFF9B9B9B),
        animationSpec = tween(animationSpec),
        label = "atMealContentTint"
    )
    val afterMealBackgroundTint by animateColorAsState(
        targetValue = if (selected == 2) Color(0xFF1BD15D) else Color.Unspecified,
        animationSpec = tween(animationSpec),
        label = "afterMealBackgroundTint"
    )
    val afterMealContentTint by animateColorAsState(
        targetValue = if (selected == 2) Color.White else Color(0xFF9B9B9B),
        animationSpec = tween(animationSpec),
        label = "afterMealContentTint"
    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onSelectMethodClicked(0) },
                painter = painterResource(id = R.drawable.method_selector_background),
                contentDescription = null,
                tint = beforeMealBackgroundTint
            )
            Icon(
                painter = painterResource(id = R.drawable.before_meal),
                contentDescription = "饭前",
                tint = beforeMealContentTint
            )
        }
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onSelectMethodClicked(1) },
                painter = painterResource(id = R.drawable.method_selector_background),
                contentDescription = null,
                tint = atMealBackgroundTint
            )
            Icon(
                painter = painterResource(id = R.drawable.at_meal),
                contentDescription = "饭中",
                tint = atMealContentTint
            )
        }
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onSelectMethodClicked(2) },
                painter = painterResource(id = R.drawable.method_selector_background),
                contentDescription = null,
                tint = afterMealBackgroundTint
            )
            Icon(
                painter = painterResource(id = R.drawable.after_meal),
                contentDescription = "饭后",
                tint = afterMealContentTint
            )
        }
    }
}

/**
 * @see TimePicker
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    hour: MutableState<Int>,
    minute: MutableState<Int>,
    visible: Boolean,
    onDismissRequest: () -> Unit
) {
    if (visible) {
        ModalBottomSheet(onDismissRequest = onDismissRequest) {
            TimePicker(hour = hour, minute = minute)
        }
    }
}

/**
 * 确认按钮。
 * @param modifier 修饰符
 * @param enabled 按钮是否可用,对应[NewRemindViewModel.buttonEnabled]
 * @param buttonHeight 按钮高度
 * @param onNextClicked 点击事件，执行的函数是[NewRemindViewModel.onCommitButtonClicked]
 * @author HinanawiYuzu
 */
@Composable
private fun CommitButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    buttonHeight: Dp,
    onNextClicked: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { hasPermission ->
        onNextClicked(hasPermission)
    }
    CommonButton(
        modifier = modifier
            .fillMaxWidth()
            .border(
                brush = transparentButtonBorderGradient,
                width = 1.5.dp,
                shape = RoundedCornerShape(percent = 15)
            )
            .height(buttonHeight),
        buttonTextRes = R.string.confirm_button_text,
        onButtonClicked = {
            if (!context.getSystemService(NotificationManager::class.java).areNotificationsEnabled()) {
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    return@CommonButton
                } else {
                    showLongToast(context, "不开启通知权限的话，无法正常使用提醒功能。")
                    val intent = Intent().apply {
                        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    }
                    context.startActivity(intent)
                }
            } else {
                onNextClicked(true)
            }
        },
        enabled = enabled,
        buttonColors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFB4E3CC),
            disabledContainerColor = Color(0x66FFFFFF)
        ),
        fontColors = Color.Black
    )
}