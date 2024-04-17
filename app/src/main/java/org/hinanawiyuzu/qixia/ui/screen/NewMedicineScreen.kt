package org.hinanawiyuzu.qixia.ui.screen

import android.graphics.*
import android.net.*
import android.provider.*
import androidx.activity.compose.*
import androidx.activity.result.*
import androidx.activity.result.contract.*
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
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
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.*
import androidx.navigation.*
import androidx.navigation.compose.*
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.*
import org.hinanawiyuzu.qixia.ui.*
import org.hinanawiyuzu.qixia.ui.theme.*
import org.hinanawiyuzu.qixia.ui.viewmodel.*
import org.hinanawiyuzu.qixia.utils.*
import java.io.*
import java.time.*

// 用于判断用户选择的是相机还是相册
const val ALBUM = false
const val CAMERA = true

/**
 * 新增药品界面
 * @param modifier 修饰符
 * @param viewModel 详见[NewMedicineViewModel]
 * @param navController 导航控制器
 * @see NewMedicineViewModel
 * @author HinanawiYuzu
 */
@Composable
fun NewMedicineScreen(
    modifier: Modifier = Modifier,
    viewModel: NewMedicineViewModel = viewModel(factory = AppViewModelProvider.factory),
    navController: NavHostController = rememberNavController()
) {
    val selectorHeight: Dp = 75.dp
    val screenWidthDp: Dp = LocalConfiguration.current.screenWidthDp.dp
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
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {
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
                        userInput = viewModel.medicineName,
                        registrationCertificateNumber = viewModel.inputRegistrationCertificateNumber,
                        onUserInputChanged = viewModel::onMedicineNameChanged,
                        onInputRegistrationCertificateNumberChanged =
                        viewModel::onInputRegistrationCertificateNumberChanged,
                        startSearch = viewModel::startSearch
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "库存",
                                style = TextStyle(
                                    fontSize = FontSize.bigSize
                                )
                            )
                            InventorySelector(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(selectorHeight),
                                userInput = viewModel.inventory,
                                onUserInputChanged = viewModel::onInventoryChanged
                            )
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "过期时间",
                                style = TextStyle(
                                    fontSize = FontSize.bigSize
                                )
                            )
                            ExpiryDateSelector(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(selectorHeight),
                                expiryDate = viewModel.expiryDate,
                                onExpiryDatePickerConfirmButtonClicked =
                                viewModel::onExpiryDatePickerConfirmButtonClicked
                            )
                        }
                    }
                }
                ImageSelector(
                    changeImageUri = viewModel::changeImageUri
                )
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-90).dp),
            visible = viewModel.showSnackBar,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Snackbar {
                Text(text = "未找到该药品，请检查输入是否正确。")
            }
        }
        NextButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(15.dp)
                .fillMaxWidth(),
            enabled = viewModel.buttonEnabled,
            buttonHeight = 70.dp,
            onNextClicked = { viewModel.onNextButtonClicked(navController) }
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
            text = "新增药品",
            style = TextStyle(
                fontSize = FontSize.veryLargeSize,
            )
        )
        Spacer(modifier = Modifier.size(40.dp))
    }
}

/**
 * 选择药品名称
 * @param modifier 修饰符
 * @param userInput 用户选择的药品名称,对应[NewMedicineViewModel.medicineName]
 * @param registrationCertificateNumber 用户输入的注册证号,对应[NewMedicineViewModel.inputRegistrationCertificateNumber]
 * @param onUserInputChanged 用户输入药品名称时的回调,对应[NewMedicineViewModel.onMedicineNameChanged]
 * @param onInputRegistrationCertificateNumberChanged 用户输入注册证号时的回调,对应[NewMedicineViewModel.onInputRegistrationCertificateNumberChanged]
 * @param startSearch 点击搜索按钮时的回调,对应[NewMedicineViewModel.startSearch]
 */
@Composable
private fun MedicineSelector(
    modifier: Modifier = Modifier,
    userInput: String,
    registrationCertificateNumber: String,
    onUserInputChanged: (String) -> Unit,
    onInputRegistrationCertificateNumberChanged: (String) -> Unit,
    startSearch: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val containerColor = Color(0xFFF4FFF8)
    OutlinedTextField(
        modifier = modifier,
        shape = RoundedCornerShape(percent = 40),
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.new_remind_screen_add_medicine),
                contentDescription = "药物名称"
            )
        },
        trailingIcon = {
            Image(
                modifier = Modifier.clickable { showDialog = true },
                painter = painterResource(id = R.drawable.search_icon_green_gradient),
                contentDescription = "通过注册证号来搜索药物"
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
        ),
        supportingText = {
            Text(
                text = "建议点击右边图标，通过输入注册证号来导入药物",
                fontSize = FontSize.tinySize
            )
        },
        singleLine = true,
        value = userInput,
        onValueChange = onUserInputChanged
    )
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Column {
                    Text(text = "请输入注册证号或批准文号", fontSize = FontSize.bigSize)
                    Text(text = "系统会自动匹配药品名称", fontSize = FontSize.smallSize)
                }
            },
            text = {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = registrationCertificateNumber,
                    onValueChange = onInputRegistrationCertificateNumberChanged,
                    placeholder = { Text(text = "例:H20080599") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    startSearch()
                }) {
                    Text(text = "确定")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(text = "取消")
                }
            }
        )
    }
}

/**
 * 选择库存
 * @param modifier 修饰符
 * @param userInput 用户输入的库存,对应[NewMedicineViewModel.inventory]
 * @param onUserInputChanged 用户输入库存时的回调,对应[NewMedicineViewModel.onInventoryChanged]
 * @author HinanawiYuzu
 */
@Composable
private fun InventorySelector(
    modifier: Modifier = Modifier,
    userInput: String,
    onUserInputChanged: (String) -> Unit
) {
    val containerColor = Color(0xFFF4FFF8)
    OutlinedTextField(
        modifier = modifier,
        shape = RoundedCornerShape(percent = 40),
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.new_remind_screen_leading_icon),
                contentDescription = "药物库存"
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        singleLine = true,
        value = userInput,
        onValueChange = onUserInputChanged
    )
}

/**
 * 选择过期时间
 * @param modifier 修饰符
 * @param expiryDate 用户选择的过期时间,对应[NewMedicineViewModel.expiryDate]
 * @param onExpiryDatePickerConfirmButtonClicked 用户点击确定按钮时的回调,对应[NewMedicineViewModel.onExpiryDatePickerConfirmButtonClicked]
 * @author HinanawiYuzu
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpiryDateSelector(
    modifier: Modifier = Modifier,
    expiryDate: LocalDate?,
    onExpiryDatePickerConfirmButtonClicked: (Long?) -> Unit
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
            text = "${expiryDate?.year ?: "    "}年${expiryDate?.monthValue ?: "  "}月${expiryDate?.dayOfMonth ?: "  "}日",
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
                    onExpiryDatePickerConfirmButtonClicked(selectedTimeMillis)
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
                state = datePickerState
            )
        }
    }
}

/**
 * @param changeImageUri 用于改变viewModel里的Uri。如果把ViewModel里的Uri传入该函数的话，那么会导致在重组之前Uri为null，需要用户二次点击的情况。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageSelector(
    modifier: Modifier = Modifier,
    changeImageUri: (Uri?) -> Unit
) {
    val context = LocalContext.current
    var pictureProperty by remember { mutableStateOf(ALBUM) }
    var visible by remember { mutableStateOf(false) }
    var imageState by remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    /*
    本来想尝试把这些rememberLauncher什么的封装成Utils，结果发现涉及到Composable函数根本没办法封装。
    看来以后遇到图片的情况只能每次都多写一些了。
     */
    // 用于启动相机拍照
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { isSuccess ->
        if (isSuccess) {
            // 从相机拍照成功后，将图片显示在 Image 组件中
            // 这里的卡顿我想应该是可以接受的
            val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(imageUri!!))
            imageState = bitmap
            pictureProperty = CAMERA
        }
    }
    // 用于启动相册选择图片
    val selectPictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        it?.let {
            val newUri = copyImageToAppDir(context, it, "Image_${System.currentTimeMillis()}.jpg")
            val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(newUri!!))
            imageState = bitmap
            imageUri = newUri
            pictureProperty = ALBUM
            changeImageUri(imageUri)
        }
    }
    // 用于请求相机权限
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { hasPermission ->
        if (hasPermission) {
            val contentValues = createUri()
            imageUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            changeImageUri(imageUri)
            imageUri?.let { takePictureLauncher.launch(it) }
        }
        visible = false
    }
    imageState?.let {
        Box(
            modifier = modifier.fillMaxSize(0.75f),
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            IconButton(onClick = {
                // 如果用户选择的是相机，才删除图片。
                // contentResolver不适合删除文件
                if (pictureProperty == CAMERA)
                    context.contentResolver.delete(imageUri!!, null, null)
                else {
                    val file = File(context.filesDir, imageUri!!.path!!)
                    file.delete()
                }
                imageState = null
                changeImageUri(null)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription = "删除图片"
                )
            }
        }
    } ?: Box(
        modifier = modifier
            .clickable { visible = !visible },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.image_selector_background),
            contentDescription = null
        )
        Image(
            painter = painterResource(id = R.drawable.image_selector_cross),
            contentDescription = null
        )

    }
    if (visible) {
        ModalBottomSheet(
            modifier = Modifier.height(300.dp),
            onDismissRequest = { visible = false }
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                CommonButton(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(percent = 50),
                    buttonTextRes = R.string.take_picture,
                    fontSize = FontSize.mediumLargeSize,
                    onButtonClicked = {
                        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                        visible = false
                    }
                )
                CommonButton(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(percent = 50),
                    buttonTextRes = R.string.select_image_from_album,
                    fontSize = FontSize.mediumLargeSize,
                    onButtonClicked = {
                        selectPictureLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        visible = false
                    }
                )
            }
        }
    }
}

@Composable
private fun NextButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    buttonHeight: Dp,
    onNextClicked: () -> Unit
) {
    CommonButton(
        modifier = modifier
            .fillMaxWidth()
            .border(
                brush = MyColor.transparentButtonBorderGradient,
                width = 1.5.dp,
                shape = RoundedCornerShape(percent = 15)
            )
            .height(buttonHeight),
        buttonTextRes = R.string.next_step,
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
fun NewMedicineScreenPreview() {
    QixiaTheme {
        NewMedicineScreen()
    }
}