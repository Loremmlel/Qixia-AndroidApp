package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.hinanawiyuzu.qixia.data.source.fake.fakeMedicalHistory

class FillPersonalInformationViewModel : ViewModel() {
    private val illnessSize = fakeMedicalHistory.size
    private val _uiState = MutableStateFlow(FillPersonalInformationUiState())
    val uiState = _uiState.asStateFlow()

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
        _uiState.update { currentState ->
            currentState.copy(
                age = value
            )
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
}

data class FillPersonalInformationUiState(
    val maleSelected: Boolean = true,
    val femaleSelected: Boolean = false,
    val age: String = "",
    val serialNumber: String = ""
)