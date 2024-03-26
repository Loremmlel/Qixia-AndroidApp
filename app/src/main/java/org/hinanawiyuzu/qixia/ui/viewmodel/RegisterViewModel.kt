package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.hinanawiyuzu.qixia.ui.state.RegisterUiState

class RegisterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()
    fun onAccountNameChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(accountName = value)
        }
    }

    fun onAccountPhoneChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(accountPhone = value)
        }
    }

    fun onClauseClicked() {
        //TODO: 当产品条款被点击时，进入产品条款浏览界面
    }

    fun onPrivacyPolicyClicked() {
        //TODO: 当隐私政策被点击时，进入隐私政策浏览界面
    }
}