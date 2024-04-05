package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.hinanawiyuzu.qixia.ui.screen.AppScreenState

class AppViewModel : ViewModel() {
    var appScreenState by mutableStateOf(AppScreenState.Remind)
        private set

    fun onBottomBarItemClicked(id: Int) {
        when (id) {
            0 -> appScreenState = AppScreenState.Main
            1 -> appScreenState = AppScreenState.Box
            2 -> appScreenState = AppScreenState.Remind
            3 -> appScreenState = AppScreenState.Record
            4 -> appScreenState = AppScreenState.Profile
        }
        print("clicked")
    }
}