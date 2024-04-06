package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.navigation.*
import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.data.repo.*
import org.hinanawiyuzu.qixia.data.source.fake.*
import org.hinanawiyuzu.qixia.utils.*

class FillPersonalInformationViewModel(
    private val userRepository: UserRepository
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
        // 原本这里有判断输入是否是数字的逻辑，但是我发现键盘设置为数字后，其它字符是无法输入的。
        // 没想到这方面系统都做了处理。
        // 然后发现貌似没有？ 4.4
        if (value.matches(Regex("""\d*""")) || value == "") {
            _uiState.update { currentState ->
                currentState.copy(
                    age = value,
                    isAgeError = value.toInt() !in (0..120)
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
            illnessCardsClicked.mapIndexed { index, b ->
                if (b) medicalHistory.add(index)
            }
            val user = User(
                phone = accountPhone,
                password = accountPassword,
                loginState = true,
                sexual = if (_uiState.value.maleSelected) "男" else "女",
                age = _uiState.value.age.toInt(),
                serialNumber = _uiState.value.serialNumber,
                medicalHistory = medicalHistory.toList()
            )
            userRepository.insertUser(user)
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