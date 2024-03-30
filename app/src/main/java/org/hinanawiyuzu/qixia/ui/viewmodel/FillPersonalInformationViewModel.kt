package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hinanawiyuzu.qixia.data.entity.UserInfo
import org.hinanawiyuzu.qixia.data.repo.UserInfoRepository
import org.hinanawiyuzu.qixia.data.source.fake.fakeMedicalHistory
import org.hinanawiyuzu.qixia.utils.AppRoute
import org.hinanawiyuzu.qixia.utils.LoginRoute

class FillPersonalInformationViewModel(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {
    private val illnessSize = fakeMedicalHistory.size
    private val _uiState = MutableStateFlow(FillPersonalInformationUiState())
    val uiState = _uiState.asStateFlow()
    var accountPassword: String = ""
    var accountPhone: String = ""

    //我算是怕了List了。不敢动不敢动。
    var illnessCardsClicked = mutableStateListOf<Boolean>()
        private set

    init {
        illnessCardsClicked.addAll(List(illnessSize) { false })
    }

    fun onMaleSelected() {
        if (!_uiState.value.maleSelected) {
            _uiState.update { currentState ->
                currentState.copy(
                    maleSelected = true,
                    femaleSelected = false
                )
            }
        } else
            return
    }

    fun onFemaleSelected() {
        if (!_uiState.value.femaleSelected) {
            _uiState.update { currentState ->
                currentState.copy(
                    maleSelected = false,
                    femaleSelected = true
                )
            }
        } else
            return
    }

    fun onAgeChanged(value: String) {
        // 虽然已经设置了键盘为数字，但是还是保险一点吧。
        if (value.matches(Regex("[0-9]+"))){
            _uiState.update { currentState ->
                currentState.copy(
                    age = value,
                    isAgeError = value.toInt() !in (0..120)
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isAgeError = true
                )
            }
        }
    }

    fun onSerialNumberChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                serialNumber = value
            )
        }
    }

    fun onIllnessCardClicked(id: Int) {
        illnessCardsClicked[id] = illnessCardsClicked[id].not()
        // ”其它“和”无“选项与其它选项冲突
        if (id == illnessSize - 1 || id == illnessSize - 2) {
            illnessCardsClicked.fill(false)
            illnessCardsClicked[id] = true
        } else {
            illnessCardsClicked[illnessSize - 1] = false
            illnessCardsClicked[illnessSize - 2] = false
        }
    }

    suspend fun onConfirmButtonClicked(navController: NavController) {
        if (!_uiState.value.isAgeError) {
            val medicalHistory: MutableList<Int> = mutableListOf()
            illnessCardsClicked.mapIndexed{ index, b ->
                if (b) medicalHistory.add(index)
            }
            val userInfo: UserInfo = UserInfo(
                phone = accountPhone,
                password = accountPassword,
                loginState = true,
                sexual = if (_uiState.value.maleSelected) "男" else "女",
                age = _uiState.value.age.toInt(),
                serialNumber = _uiState.value.serialNumber,
                medicalHistory = medicalHistory.toList()
            )
            userInfoRepository.insertUserInfo(userInfo)
            navController.navigate(AppRoute.AppScreen.name) {
                // 清空所有到LoginScreen的路线。inclusive为true表示包括LoginScreen
                navController.popBackStack(
                    route = LoginRoute.LoginScreen.name,
                    inclusive = true
                )
            }
        }
    }
}

data class FillPersonalInformationUiState(
    val maleSelected: Boolean = true,
    val femaleSelected: Boolean = false,
    val age: String = "",
    val isAgeError: Boolean = false,
    val serialNumber: String = ""
)