package org.hinanawiyuzu.qixia.ui.viewmodel

import android.net.*
import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.navigation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.*
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.data.repo.*
import org.hinanawiyuzu.qixia.utils.*
import java.time.*

class NewMedicineViewModel(
    private val medicineInfoRepository: MedicineInfoRepository,
    private val medicineRepoRepository: MedicineRepoRepository,
    private val application: QixiaApplication
) : ViewModel() {
    // 用户输入的药品名
    var medicineName: String by mutableStateOf("")

    // 用户输入的库存数量
    var inventory: String by mutableStateOf("")

    // 用户输入的注册证号
    var inputRegistrationCertificateNumber: String by mutableStateOf("")

    // 用户选择的过期日期
    var expiryDate: LocalDate? by mutableStateOf(null)

    // 用户选择的图片Uri
    private var imageUri by mutableStateOf<Uri?>(null)

    // 是否显示提示信息
    var showSnackBar by mutableStateOf(false)

    // 按钮是否可用
    var buttonEnabled by mutableStateOf(false)

    // 根据注册证号查到的药品剂型
    var dosageForm: String? by mutableStateOf(null)

    // 根据注册证号查到的药品规格
    var specification: String? by mutableStateOf(null)

    fun onMedicineNameChanged(value: String) {
        medicineName = value
        checkButtonEnabled()
    }

    fun onInventoryChanged(value: String) {
        if (value.matches(Regex("""\d*""")) || value == "") {
            inventory = value
        }
        checkButtonEnabled()
    }

    fun onInputRegistrationCertificateNumberChanged(value: String) {
        inputRegistrationCertificateNumber = value
    }

    fun changeImageUri(uri: Uri?) {
        imageUri = uri
        checkButtonEnabled()
    }


    fun startSearch() {
        viewModelScope.launch {
            val result =
                medicineInfoRepository
                    .getStreamByRegistrationCertificateNumber(inputRegistrationCertificateNumber.uppercase())
                    .firstOrNull()
            result?.let {
                medicineName = it.productName!!
                dosageForm = it.dosageForm
                specification = it.specification
            } ?: run {
                // 如果没查到，就显示提示信息
                showSnackBar = true
                delay(2000)
                showSnackBar = false
            }
            checkButtonEnabled()
        }
    }

    fun onExpiryDatePickerConfirmButtonClicked(millis: Long?) {
        expiryDate = millis?.toLocalDate()
        checkButtonEnabled()
    }

    fun onNextButtonClicked(navController: NavController) {
        val medicineRepo = MedicineRepo(
            name = medicineName,
            remainAmount = inventory,
            dosageForm = dosageForm,
            specification = specification,
            imageUri = imageUri!!,
            expiryDate = expiryDate!!,
            attentionMatter = null,
            userId = application.currentLoginUserId!!
        )
        viewModelScope.launch {
            medicineRepoRepository.insert(medicineRepo)
            navController.popBackStack()
        }
    }

    private fun checkButtonEnabled() {
        buttonEnabled = medicineName.isNotBlank() && inventory.isNotBlank() && expiryDate != null && imageUri != null
    }
}