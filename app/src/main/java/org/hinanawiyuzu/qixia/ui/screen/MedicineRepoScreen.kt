package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.BlurredBackground
import org.hinanawiyuzu.qixia.components.GrayLine
import org.hinanawiyuzu.qixia.data.entity.AttentionMatter
import org.hinanawiyuzu.qixia.data.entity.MedicineRepo
import org.hinanawiyuzu.qixia.ui.AppViewModelProvider
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.theme.neutral_color
import org.hinanawiyuzu.qixia.ui.viewmodel.MedicineRepoListViewModel
import org.hinanawiyuzu.qixia.ui.viewmodel.MedicineRepoViewModel
import org.hinanawiyuzu.qixia.ui.viewmodel.SortCondition
import org.hinanawiyuzu.qixia.ui.viewmodel.shared.SharedMedicineRepoIdViewModel
import org.hinanawiyuzu.qixia.utils.advancedShadow
import java.time.LocalDate


/**
 * 药品仓库清单界面
 * @param modifier 修饰符
 * @param sharedViewModel 用于新建提醒界面和药品仓库界面之间共享数据的ViewModel
 * @param viewModel 药品仓库界面的ViewModel
 * @param navController 导航控制器
 * @see MedicineRepoViewModel
 * @see SharedMedicineRepoIdViewModel
 * @author HinanawiYuzu
 */
@Composable
fun MedicineRepoScreen(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedMedicineRepoIdViewModel,
    viewModel: MedicineRepoViewModel = viewModel(factory = AppViewModelProvider.factory),
    navController: NavHostController = rememberNavController()
) {
    val screenWidthDp: Dp = LocalConfiguration.current.screenWidthDp.dp
    val allMedicineRepo by viewModel.allMedicineRepo.collectAsState()
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        BlurredBackground()
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                onBackClicked = { navController.popBackStack() },
                onDropDownMenuItemClicked = viewModel::onSortConditionChanged
            )
            GrayLine(screenWidthDp = screenWidthDp)
            SearchBox(
                modifier = Modifier.fillMaxWidth(),
                userInput = viewModel.userSearchInput,
                onUserInputChanged = viewModel::onUserSearchInputChanged,
                startSearch = viewModel::startSearch
            )
            MedicineRepoCards(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(10.dp),
                allMedicineRepo = allMedicineRepo.allMedicineRepoList,
                medicineRepos = viewModel.displayedMedicineRepo,
                selectedStates = viewModel.selectedStates,
                onSelectClicked = viewModel::toggleSelection
            )
        }
        BottomButtons(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp)
                .fillMaxWidth(),
            onAddClicked = { viewModel.onAddMedicineClicked(navController) },
            onSelectClicked = {
                viewModel.onSelectClicked(
                    navController,
                    sharedViewModel::changeMedicineRepoId
                )
            }
        )
    }
}

/**
 * 没有sharedViewModel的，能够进行删的版本.
 */
@Composable
fun MedicineRepoListScreen(
    modifier: Modifier = Modifier,
    viewModel: MedicineRepoListViewModel = viewModel(factory = AppViewModelProvider.factory),
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val screenWidthDp: Dp = LocalConfiguration.current.screenWidthDp.dp
    val allMedicineRepo by viewModel.allMedicineRepo.collectAsState()
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        BlurredBackground()
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                onBackClicked = { navController.popBackStack() },
                onDropDownMenuItemClicked = viewModel::onSortConditionChanged
            )
            GrayLine(screenWidthDp = screenWidthDp)
            SearchBox(
                modifier = Modifier.fillMaxWidth(),
                userInput = viewModel.userSearchInput,
                onUserInputChanged = viewModel::onUserSearchInputChanged,
                startSearch = viewModel::startSearch
            )
            MedicineRepoCards(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(10.dp),
                allMedicineRepo = allMedicineRepo.allMedicineRepoList,
                medicineRepos = viewModel.displayedMedicineRepo,
                selectedStates = viewModel.selectedStates,
                onSelectClicked = viewModel::toggleSelection
            )
        }
        DeleteButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp),
            enabled = viewModel.deleteButtonEnabled,
            onDeleteClicked = { viewModel.onDeleteClicked(context) },
        )
    }
}

/**
 * 药品仓库清单界面的顶部栏
 * @param modifier 修饰符
 * @param onBackClicked 返回按钮点击事件
 * @param onDropDownMenuItemClicked 下拉菜单点击事件,对应[MedicineRepoViewModel.onSortConditionChanged]
 * @author HinanawiYuzu
 */
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onDropDownMenuItemClicked: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = SortCondition.entries.map { it.convertToString() }
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
            text = "药品清单",
            style = TextStyle(
                fontSize = FontSize.veryLargeSize,
            )
        )
        Box {
            Column(
                modifier = Modifier
                    .clickable { expanded = !expanded },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.sort_icon),
                    contentDescription = "排序",
                    tint = Color.Unspecified
                )
                Text(
                    text = "排序",
                    style = TextStyle(
                        fontSize = FontSize.tinySize,
                    )
                )
            }
            DropdownMenu(
                modifier = Modifier.align(Alignment.BottomCenter),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onDropDownMenuItemClicked(options.indexOf(option))
                        },
                        text = {
                            Text(
                                text = option,
                                style = TextStyle(fontSize = FontSize.tinySize)
                            )
                        }
                    )
                }
            }
        }
    }
}

/**
 * 搜索框
 * @param modifier 修饰符
 * @param userInput 用户输入,对应[MedicineRepoViewModel.userSearchInput]
 * @param onUserInputChanged 用户输入改变事件,对应[MedicineRepoViewModel.onUserSearchInputChanged]
 * @param startSearch 开始搜索事件,对应[MedicineRepoViewModel.startSearch]
 * @author HinanawiYuzu
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchBox(
    modifier: Modifier = Modifier,
    userInput: String?,
    onUserInputChanged: (String) -> Unit,
    startSearch: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        modifier = modifier
            .padding(10.dp)
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(percent = 40),
        textStyle = TextStyle(fontSize = FontSize.smallSize),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search_icon),
                contentDescription = "搜索"
            )
        },
        placeholder = {
            Text(
                text = "搜索",
                style = TextStyle(fontSize = FontSize.normalSize, color = Color.Gray)
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = androidx.compose.ui.text.input.ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                focusRequester.freeFocus()
                startSearch()
            }
        ),
        singleLine = true,
        value = userInput ?: "",
        onValueChange = onUserInputChanged
    )
}

/**
 * 药品仓库卡片列表
 * @param modifier 修饰符
 * @param allMedicineRepo 所有药品仓库信息,对应[MedicineRepoViewModel.allMedicineRepo]
 * @param medicineRepos 显示的药品仓库信息,对应[MedicineRepoViewModel.displayedMedicineRepo]
 * @param selectedStates 选中状态,对应[MedicineRepoViewModel.selectedStates]
 * @param onSelectClicked 选择事件,对应[MedicineRepoViewModel.toggleSelection]
 * @author HinanawiYuzu
 */
@Composable
private fun MedicineRepoCards(
    modifier: Modifier = Modifier,
    allMedicineRepo: List<MedicineRepo>,
    medicineRepos: List<MedicineRepo>,
    selectedStates: List<Boolean>,
    onSelectClicked: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        medicineRepos.forEach { medicineRepo ->
            // 获取位于allMedicineRepo中的索引
            val index = allMedicineRepo.indexOf(medicineRepo)
            MedicineRepoCard(
                modifier = Modifier.fillMaxWidth(),
                medicineRepo = medicineRepo,
                isSelected = selectedStates[index],
                onSelectClicked = { onSelectClicked(index) }
            )
        }
        if (medicineRepos.isEmpty()) {
            Text(
                text = "没有找到药物！",
                fontSize = FontSize.extraLargeSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 药品仓库卡片
 * @param modifier 修饰符
 * @param medicineRepo 药品仓库信息
 * @param isSelected 是否选中,对应[MedicineRepoViewModel.selectedStates]的一个元素
 * @param onSelectClicked 选择事件,对应[MedicineRepoViewModel.toggleSelection]
 * @author HinanawiYuzu
 */
@Composable
fun MedicineRepoCard(
    modifier: Modifier = Modifier,
    medicineRepo: MedicineRepo,
    isSelected: Boolean,
    onSelectClicked: () -> Unit
) {
    val currentDate: LocalDate = LocalDate.now()
    val expiryDate: LocalDate = medicineRepo.expiryDate
    val remainAmount = medicineRepo.remainAmount.replace(Regex("\\D"), "").toInt()
    // 警告等级 0: 正常 1: 剩余数量不足 2: 已过期 3: 剩余数量不足且已过期
    val warningMode: Int =
        if (expiryDate.isBefore(currentDate) && remainAmount <= 10) 3
        else if (expiryDate.isBefore(currentDate)) 2
        else if (remainAmount <= 10) 1
        else 0
    Row(
        modifier = modifier
            .advancedShadow(
                alpha = 0.4f,
                cornersRadius = 10.dp,
                shadowBlurRadius = 3.dp,
                offsetY = 3.dp
            )
            .clip(RoundedCornerShape(percent = 10))
            .height(90.dp)
            .background(Color.White)
    ) {
        when (warningMode) {
            0 -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(color = neutral_color)
                ) {}
            }

            1 -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(color = Color(0xFFE3DFB8))
                ) {}
            }

            2 -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(color = Color(0xFFE3B8B8))
                ) {}
            }

            3 -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.verticalGradient(
                                colorStops = arrayOf(
                                    Pair(0.0f, Color(0xFFE3B8B8)),
                                    Pair(0.5f, Color(0xFFE3B8B8)),
                                    Pair(0.5f, Color(0xFFE3DFB8)),
                                    Pair(1.0f, Color(0xFFE3DFB8))
                                )
                            )
                        )
                ) {}
            }

            else -> Unit
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(28.5f),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, top = 5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    text = medicineRepo.name,
                    style = TextStyle(
                        fontSize = FontSize.bigSize,
                        color = Color(0xFF59597C),
                        fontWeight = FontWeight.ExtraBold,
                    )
                )
                if (warningMode in (2..3)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "已过期",
                            color = Color.Red,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = FontSize.smallSize
                        )
                        Image(
                            painter = painterResource(id = R.drawable.wrong_circle),
                            contentDescription = "药物已过期！"
                        )
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "保质期内",
                            color = Color(0xFF88879C),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = FontSize.smallSize
                        )
                        Image(
                            painter = painterResource(id = R.drawable.right_circle),
                            contentDescription = "药物在保质期内"
                        )
                    }
                }
                Text(
                    text = "剩余${remainAmount}",
                    style = TextStyle(
                        fontSize = FontSize.normalSize,
                        color = if (warningMode == 1 || warningMode == 3) Color.Red
                        else Color(0xFF59597C),
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
            Row(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (warningMode in (2..3)) {
                    Text(
                        text = "${medicineRepo.expiryDate}    已过期",
                        color = Color.Red,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = FontSize.smallSize
                    )
                } else {
                    Text(
                        text = "${medicineRepo.expiryDate}    过期",
                        color = Color.Gray,
                        fontWeight = FontWeight.Light,
                        fontSize = FontSize.smallSize
                    )
                }
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        modifier = Modifier
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                            ) {
                                onSelectClicked()
                            },
                        painter = painterResource(id = R.drawable.green_frame),
                        contentDescription = null
                    )
                    if (isSelected) {
                        Image(
                            painter = painterResource(id = R.drawable.green_tick),
                            contentDescription = "已选中"
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                if (medicineRepo.attentionMatter != null && medicineRepo.attentionMatter != AttentionMatter.None) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(percent = 20))
                            .background(color = Color(0x26FE9B4B)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = medicineRepo.attentionMatter.convertToString(),
                            color = Color(0xFFFE9B4B),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = FontSize.smallSize
                        )
                    }
                }
                if (warningMode == 1 || warningMode == 3) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(percent = 20))
                            .background(color = Color(0x26FE9B4B)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = "请及时补充药物!",
                            color = Color.Red,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = FontSize.smallSize
                        )
                    }
                }
                Text(
                    text = if (warningMode in (2..3)) "不可服用" else "",
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium,
                    fontSize = FontSize.smallSize
                )
            }
        }
    }
}

/**
 * 底部按钮
 * @param modifier 修饰符
 * @param onAddClicked 新增按钮点击事件,对应[MedicineRepoViewModel.onAddMedicineClicked]
 * @param onSelectClicked 选择按钮点击事件,对应[MedicineRepoViewModel.onSelectClicked]
 * @author HinanawiYuzu
 */
@Composable
private fun BottomButtons(
    modifier: Modifier = Modifier,
    onAddClicked: () -> Unit,
    onSelectClicked: () -> Unit
) {
    val buttonBrush: Brush = Brush.verticalGradient(
        colors = listOf(Color(0xFFB8E3CD), Color.White)
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier
                .advancedShadow(
                    alpha = 0.4f,
                    shadowBlurRadius = 5.dp,
                    cornersRadius = 10.dp,
                    offsetY = 3.dp
                )
                .width(150.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(percent = 20))
                .clickable { onAddClicked() }
                .background(buttonBrush),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "新增",
                style = TextStyle(
                    fontSize = FontSize.extraLargeSize,
                    fontWeight = FontWeight.Medium
                )
            )
        }
        Column(
            modifier = Modifier
                .advancedShadow(
                    alpha = 0.4f,
                    shadowBlurRadius = 5.dp,
                    cornersRadius = 10.dp,
                    offsetY = 3.dp
                )
                .width(150.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(percent = 20))
                .clickable { onSelectClicked() }
                .background(buttonBrush),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "选择",
                style = TextStyle(
                    fontSize = FontSize.extraLargeSize,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
private fun DeleteButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onDeleteClicked: () -> Unit
) {
    var showConfirmDeleteWindow by remember { mutableStateOf(false) }
    val unableBrush: Brush = Brush.verticalGradient(
        colors = listOf(Color(249, 239, 222), Color(249, 239, 222))
    )
    val enableBrush: Brush = Brush.linearGradient(
        colors = listOf(Color(0xB5FECE98), Color(0xBFF59A19))
    )
    Column(
        modifier = modifier
            .width(150.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(percent = 20))
            .clickable(enabled = enabled) { showConfirmDeleteWindow = true }
            .background(brush = if (enabled) enableBrush else unableBrush),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "删除",
            style = TextStyle(
                fontSize = FontSize.extraLargeSize,
                fontWeight = FontWeight.Medium,
                color = Color.Red
            )
        )
    }
    if (showConfirmDeleteWindow) {
        AlertDialog(
            onDismissRequest = { showConfirmDeleteWindow = false },
            title = {
                Text(
                    text = "确认删除",
                    style = TextStyle(
                        fontSize = FontSize.extraLargeSize,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(
                    text = "确认删除选中的药品？(操作不可恢复)",
                    style = TextStyle(
                        fontSize = FontSize.normalSize,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            confirmButton = {
                Button(onClick = {
                    onDeleteClicked()
                    showConfirmDeleteWindow = false
                }) {
                    Text(
                        text = "确认",
                        style = TextStyle(
                            fontSize = FontSize.normalSize,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmDeleteWindow = false }) {
                    Text(
                        text = "取消",
                        style = TextStyle(
                            fontSize = FontSize.normalSize,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        )
    }
}