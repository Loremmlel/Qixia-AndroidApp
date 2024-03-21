package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ForgetPasswordViewModel: ViewModel() {
    var accountPhone by mutableStateOf("")
        private set
    fun onAccountPhoneChanged(value: String) {
        accountPhone = value
    }
}