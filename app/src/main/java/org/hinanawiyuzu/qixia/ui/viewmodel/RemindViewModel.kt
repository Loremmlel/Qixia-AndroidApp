package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.lifecycle.ViewModel
import java.time.LocalDateTime

class RemindViewModel : ViewModel() {
    //TODO: 除了以系统时间为基准外，也许需要联网获取数据.
    //TODO: 这个应该可以更改，然后根据日期显示昨天或者明天的预期提醒。
    var currentTime: LocalDateTime = LocalDateTime.now()
}


