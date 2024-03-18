package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegisterViewModel: ViewModel() {
    var accountName by mutableStateOf("")
        private set
    var accountPhone by mutableStateOf("")
        private set
    fun onAccountNameChanged(value: String) {
        accountName = value
    }

    fun onAccountPhoneChanged(value: String) {
        accountPhone = value
    }

    fun onClauseClicked() {
        //TODO: 当产品条款被点击时，进入产品条款浏览界面
    }

    fun onPrivacyPolicyClicked() {
        //TODO: 当隐私政策被点击时，进入隐私政策浏览界面
    }
}