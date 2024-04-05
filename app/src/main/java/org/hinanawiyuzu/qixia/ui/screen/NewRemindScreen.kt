package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.BlurredBackground
import org.hinanawiyuzu.qixia.components.CommonButton
import org.hinanawiyuzu.qixia.components.GrayLine
import org.hinanawiyuzu.qixia.components.TimePicker
import org.hinanawiyuzu.qixia.data.entity.MedicineFrequency
import org.hinanawiyuzu.qixia.data.entity.TakeMethod
import org.hinanawiyuzu.qixia.ui.AppViewModelProvider
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.theme.MyColor
import org.hinanawiyuzu.qixia.ui.theme.MyColor.transparentButtonBorderGradient
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.theme.secondary_color
import org.hinanawiyuzu.qixia.ui.viewmodel.NewRemindViewModel
import org.hinanawiyuzu.qixia.ui.viewmodel.shared.SharedBetweenMedicineRepoAndNewRemindViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun NewRemindScreen(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedBetweenMedicineRepoAndNewRemindViewModel,
    viewModel: NewRemindViewModel = viewModel(factory = AppViewModelProvider.factory),
    navController: NavHostController = rememberNavController(),
) {
    val selectorHeight: Dp = 35.dp
    val screenWidthDp: Dp = LocalConfiguration.current.screenWidthDp.dp
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
                onBackClicked = {navController.popBackStack()}
            )
            GrayLine(screenWidthDp = screenWidthDp)
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .verticalScroll(state = rememberScrollState()),
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
            onNextClicked = { viewModel.onCommitButtonClicked(navController) }
        )
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
            text = "新增提醒",
            style = TextStyle(
                fontSize = FontSize.veryLargeSize,
            )
        )
        Spacer(modifier = Modifier.size(40.dp))
    }
}

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

//我仔细想了下，发现还是分开来，逻辑会简单好处理一些。
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

@Composable
private fun CommitButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    buttonHeight: Dp,
    onNextClicked: () -> Unit
) {
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
        onButtonClicked = onNextClicked,
        enabled = enabled,
        buttonColors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFB4E3CC),
            disabledContainerColor = Color(0x66FFFFFF)
        ),
        fontColors = Color.Black
    )
}

@Preview
@Composable
private fun NewRemindScreenPreview() {
    QixiaTheme {

    }
}