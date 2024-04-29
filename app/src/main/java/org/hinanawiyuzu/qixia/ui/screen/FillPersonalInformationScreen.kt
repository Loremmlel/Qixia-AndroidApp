package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.*
import androidx.navigation.*
import androidx.navigation.compose.*
import kotlinx.coroutines.*
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.*
import org.hinanawiyuzu.qixia.data.source.fake.*
import org.hinanawiyuzu.qixia.ui.*
import org.hinanawiyuzu.qixia.ui.theme.*
import org.hinanawiyuzu.qixia.ui.theme.MyColor.themeHorizontalGradient
import org.hinanawiyuzu.qixia.ui.viewmodel.*

@Composable
fun FillPersonalInformationScreen(
    modifier: Modifier = Modifier,
    viewModel: FillPersonalInformationViewModel =
        viewModel(factory = AppViewModelProvider.factory),
    navController: NavController = rememberNavController(),
    backStackEntry: NavBackStackEntry
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val uiState by viewModel.uiState.collectAsState()
    val circleOffsetX = (-20).dp
    val circleOffsetY = 50.dp
    val accountPassword: String? = backStackEntry.arguments?.getString("accountPassword")
    val accountPhone: String? = backStackEntry.arguments?.getString("accountPhone")
    // 能到这个页面的肯定是新注册，而不是找回密码。所以这两个值必定不为空。
    viewModel.accountPassword = accountPassword!!
    viewModel.accountPhone = accountPhone!!
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = themeHorizontalGradient)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxSize()
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = circleOffsetX, y = circleOffsetY),
                painter = painterResource(id = R.drawable.background_circle),
                contentDescription = null
            )
            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = circleOffsetX + 30.dp, y = circleOffsetY + 45.dp),
                painter = painterResource(id = R.drawable.background_circle),
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStartPercent = 10, topEndPercent = 10))
                .background(Color.White)
                .padding(start = 15.dp, end = 15.dp, bottom = 30.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .requiredHeight((screenHeight * 0.8).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBackIcon(
                modifier = Modifier
                    .align(Alignment.Start)
                    .offset(y = 15.dp)
            ) {
                //TODO: 跳转到哪？
                //暂时写个返回逻辑吧
                navController.navigateUp()
            }
            InputArea(
                modifier = Modifier.weight(0.2f),
                maleSelected = uiState.maleSelected,
                femaleSelected = uiState.femaleSelected,
                name = uiState.name,
                age = uiState.age,
                isAgeError = uiState.isAgeError,
                isNameError = uiState.isNameError,
                serialNumber = uiState.serialNumber,
                onMaleClicked = { viewModel.onMaleSelected() },
                onFemaleClicked = { viewModel.onFemaleSelected() },
                onAgeChanged = { viewModel.onAgeChanged(it) },
                onSerialNumberChanged = { viewModel.onSerialNumberChanged(it) },
                onNameChanged = { viewModel.onNameChanged(it) }
            )
            MedicalHistory(
                modifier = Modifier
                    .weight(0.15f)
                    .padding(bottom = 30.dp),
                isIllnessCardClicked = viewModel.illnessCardsClicked,
                onIllnessCardClicked = { viewModel.onIllnessCardClicked(it) }
            )
            ConfirmButton(
                modifier = Modifier
                    .weight(0.05f)
                    .fillMaxWidth(0.8f),
                onButtonClicked = {
                    coroutineScope.launch {
                        viewModel.onConfirmButtonClicked(navController, context)
                    }
                }
            )
        }
    }
}

/**
 * 顶部的返回箭头。
 * @param modifier 修饰符
 * @param onBackIconClicked 按钮的回调函数。
 * @author HinanawiYuzu
 */
@Composable
private fun TopBackIcon(
    modifier: Modifier = Modifier,
    onBackIconClicked: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onBackIconClicked
    ) {
        Icon(
            painter = painterResource(id = R.drawable.left_arrow),
            contentDescription = stringResource(R.string.back)
        )
    }
}

/**
 * 用户自定义输入的部分
 * @param modifier 修饰符
 * @param maleSelected 性别“男”是否被选中
 * @param femaleSelected 性别“女”是否被选中
 * @param age 用户填入的年龄
 * @param serialNumber 用户填入的智能药箱序列码
 * @param onMaleClicked 性别“男”选中的回调函数
 * @param onFemaleClicked 性别“女”选中的回调函数
 * @param onAgeChanged 年龄输入框的回调函数
 * @param onSerialNumberChanged 智能药箱序列码输入框的回调函数
 * @author HinanawiYuzu
 */
@Composable
private fun InputArea(
    modifier: Modifier = Modifier,
    maleSelected: Boolean,
    femaleSelected: Boolean,
    name: String,
    age: String,
    serialNumber: String,
    isAgeError: Boolean,
    isNameError: Boolean,
    onMaleClicked: () -> Unit,
    onFemaleClicked: () -> Unit,
    onAgeChanged: (String) -> Unit,
    onSerialNumberChanged: (String) -> Unit,
    onNameChanged: (String) -> Unit
) {
    Column(
        modifier = modifier
            .padding(start = 20.dp, top = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "个人信息",
            style = TextStyle(fontSize = 30.sp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "性别",
                style = TextStyle(fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Text(text = "男")
            RadioButton(selected = maleSelected, onClick = onMaleClicked)
            Text(text = "女")
            RadioButton(selected = femaleSelected, onClick = onFemaleClicked)
        }
        CommonInputFieldWithoutLeadingIcon(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            placeholderTextRes = R.string.name,
            isError = isNameError,
            errorMessage = "请输入姓名",
            onValueChanged = { onNameChanged(it) }
        )
        CommonInputFieldWithoutLeadingIcon(
            modifier = Modifier.fillMaxWidth(),
            value = age,
            placeholderTextRes = R.string.fill_personal_information_screen_age,
            isError = isAgeError,
            errorMessage = "请输入正确的年龄",
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChanged = { onAgeChanged(it) }
        )
        CommonInputFieldWithoutLeadingIcon(
            modifier = Modifier.fillMaxWidth(),
            value = serialNumber,
            placeholderTextRes = R.string.fill_personal_information_serial_number,
            onValueChanged = { onSerialNumberChanged(it) }
        )
    }
}

/**
 * 用户选择病史的区域。自定义的多选+可滚动列表。美中不足的地方在于一列固定显示三个标签，原因是原版的LazyHorizontalStaggeredGrid不够好用。
 * @param modifier 修饰符
 * @param isIllnessCardClicked 每个病史标签是否被点击的标识列表。
 * @param onIllnessCardClicked 病史标签被点击的回调函数
 * @author HinanawiYuzu
 */
@Composable
private fun MedicalHistory(
    modifier: Modifier = Modifier,
    isIllnessCardClicked: MutableList<Boolean>,
    onIllnessCardClicked: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 40.dp, end = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = "病史",
            style = TextStyle(
                fontSize = FontSize.bigSize,
                fontWeight = FontWeight.SemiBold
            )
        )
        Column(
            modifier = Modifier
                .verticalScroll(
                    state = rememberScrollState()
                )
        ) {
            val size = fakeMedicalHistory.size
            repeat(size / 3) {
                Row(
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IllnessCard(
                        id = it * 3,
                        isClicked = isIllnessCardClicked[it * 3],
                        onCardClicked = onIllnessCardClicked,
                        illness = fakeMedicalHistory[it * 3]
                    )
                    IllnessCard(
                        id = it * 3 + 1,
                        isClicked = isIllnessCardClicked[it * 3 + 1],
                        onCardClicked = onIllnessCardClicked,
                        illness = fakeMedicalHistory[it * 3 + 1]
                    )
                    IllnessCard(
                        id = it * 3 + 2,
                        isClicked = isIllnessCardClicked[it * 3 + 2],
                        onCardClicked = onIllnessCardClicked,
                        illness = fakeMedicalHistory[it * 3 + 2]
                    )
                }
            }
            if (size.toDouble() / 3 != (size / 3).toDouble()) {
                Row(
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IllnessCard(
                        id = (size - 2) * 3,
                        isClicked = isIllnessCardClicked[(size - 2) * 3],
                        onCardClicked = onIllnessCardClicked,
                        illness = fakeMedicalHistory[size - 2]
                    )
                    IllnessCard(
                        id = (size - 1) * 3,
                        isClicked = isIllnessCardClicked[(size - 1) * 3],
                        onCardClicked = onIllnessCardClicked,
                        illness = fakeMedicalHistory[size - 1]
                    )
                }
            }
        }
    }
}

/**
 * 单个病史标签
 * @param modifier 修饰符
 * @param id 每个病史标签分配到的状态列表下标值。
 * @param isClicked 病史标签是否被点击
 * @param onCardClicked 病史标签被点击的回调函数
 * @param illness 病史标签显示的疾病名称
 */
@Composable
private fun IllnessCard(
    modifier: Modifier = Modifier,
    id: Int,
    isClicked: Boolean = false,
    onCardClicked: (Int) -> Unit,
    illness: String
) {
    OutlinedCard(
        modifier = modifier
            .clickable { onCardClicked(id) },
        colors = CardDefaults.cardColors(
            containerColor = if (!isClicked) Color.White else secondary_color
        ),
        border = BorderStroke(2.dp, if (!isClicked) neutral_color else Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier,
                text = illness,
                textAlign = TextAlign.Center,
                maxLines = 1,
                style = TextStyle(color = if (!isClicked) Color.Black else Color.White)
            )
        }
    }
}

@Composable
private fun ConfirmButton(
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit
) {
    CommonButton(
        modifier = modifier,
        buttonTextRes = R.string.confirm_button_text,
        onButtonClicked = onButtonClicked
    )
}

@Preview
@Composable
private fun FillPersonalInformationScreenPreview() {
    QixiaTheme {

    }
}