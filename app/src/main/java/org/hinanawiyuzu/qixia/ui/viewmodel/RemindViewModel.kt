package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.lifecycle.ViewModel
import java.time.LocalDateTime

class RemindViewModel : ViewModel() {
    //TODO: 除了以系统时间为基准外，也许需要联网获取数据
    val currentSystemTime = LocalDateTime.now()
}


