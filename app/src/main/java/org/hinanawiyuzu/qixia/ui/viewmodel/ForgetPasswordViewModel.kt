package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*

class ForgetPasswordViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ForgetPasswordUiState())
    val uiState = _uiState.asStateFlow()
    fun onAccountPhoneChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(accountPhone = value)
        }
    }
}

data class ForgetPasswordUiState(
    val accountPhone: String = ""
)