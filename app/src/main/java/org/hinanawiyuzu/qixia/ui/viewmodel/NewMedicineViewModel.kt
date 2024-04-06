package org.hinanawiyuzu.qixia.ui.viewmodel

import android.net.*
import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.navigation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.data.repo.*
import java.time.*

class NewMedicineViewModel(
    private val medicineInfoRepository: MedicineInfoRepository,
    private val medicineRepoRepository: MedicineRepoRepository
) : ViewModel() {
    var medicineName: String by mutableStateOf("")
    var inventory: String by mutableStateOf("")
    var inputRegistrationCertificateNumber: String by mutableStateOf("")
    var expiryDate: LocalDate? by mutableStateOf(null)
    private var imageUri by mutableStateOf<Uri?>(null)
    var showSnackBar by mutableStateOf(false)
    var buttonEnabled by mutableStateOf(false)
    var dosageForm: String? by mutableStateOf(null)
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
                medicineInfoRepository.queryByRegistrationCertificateNumber((inputRegistrationCertificateNumber.uppercase()))
                    .firstOrNull()
            result?.let {
                medicineName = it.productName!!
                dosageForm = it.dosageForm
                specification = it.specification
            } ?: run {
                showSnackBar = true
                delay(2000)
                showSnackBar = false
            }
            checkButtonEnabled()
        }
    }

    fun onExpiryDatePickerConfirmButtonClicked(millis: Long?) {
        expiryDate = millis?.let {
            Instant
                .ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
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
            attentionMatter = null
        )
        viewModelScope.launch {
            medicineRepoRepository.insertMedicineRepo(medicineRepo)
            navController.popBackStack()
        }
    }

    private fun checkButtonEnabled() {
        buttonEnabled = medicineName.isNotBlank() && inventory.isNotBlank() && expiryDate != null && imageUri != null
    }
}