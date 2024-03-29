package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel


class VerificationCodeViewModel : ViewModel() {
    // 这种涉及到List的，好像我还不会放到UiState里……😭
    private val inputFieldNum = 4
    var verificationCodes = mutableStateListOf("", "", "", "")
        private set
    val focusRequesters = List(4) { FocusRequester() }

    /**
     * 适用于inputFieldNum个输入框的输入处理函数。
     *
     * 功能：当用户输入一个
     */
    fun onTextFieldsInput(value: String) {
        // 预期情况，split[0]应当是用户输入的数字，而split[1]是附带的id信息。
        val split = value.split("#")
        val id = split[1].toInt()
        if (split[0].isEmpty()) {
            // 采用这种别扭写法的原因是mutableStateOf的机制。
            // 如果要检测的是一个Array或者MutableList，那么必须要这个集合本身发生setValue操作才能刷新。
            verificationCodes[id] = ""
            requestFocusOfNoInput(
                if (id == 0)
                    0
                else
                    id - 1
            )
            return
        }
        // 一个输入框只能输入一个数字。同时只能输入数字。
        if (split[0].length > 1 || split[0].toCharArray()[0] !in '0'..'9')
            return
        verificationCodes[id] = split[0]
        requestFocusOfNoInput(
            if (id == 3)
                3
            else
                id + 1
        )
    }

    /**
     * 寻找第一个还未填写的输入框并请求焦点
     * @param target 目标文本框id。我加这个的目的，单纯只是考虑到退格的情况。
     * 退格时，输入的文本为空，那么焦点应当转到第一个为空的输入框的前一个输入框。
     */
    private fun requestFocusOfNoInput(target: Int) {
        if (target >= 0)
            focusRequesters[target].requestFocus()
        else {
            for (i in 0..<inputFieldNum) {
                if (verificationCodes[i].isEmpty()) {
                    focusRequesters[i].requestFocus()
                    break
                }
            }
        }
    }

    fun onBackspaceClicked(id: Int) {
        // 如果该id的输入框里已经有数据的话，交给onTextFieldInput函数处理。
        if (id != 0 && verificationCodes[id].isEmpty()) {
            verificationCodes[id - 1] = ""
            requestFocusOfNoInput(id - 1)
        } else
            return
    }

}