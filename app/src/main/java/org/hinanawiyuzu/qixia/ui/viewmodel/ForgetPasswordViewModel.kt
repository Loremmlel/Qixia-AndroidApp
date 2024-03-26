package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.hinanawiyuzu.qixia.ui.state.ForgetPasswordUiState

class ForgetPasswordViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ForgetPasswordUiState())
    val uiState = _uiState.asStateFlow()
    fun onAccountPhoneChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(accountPhone = value)
        }
    }
}