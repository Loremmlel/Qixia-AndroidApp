package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.hinanawiyuzu.qixia.QixiaApplication
import org.hinanawiyuzu.qixia.data.repo.UserRepository

class BoxViewModel(
    private val userRepository: UserRepository,
    private val application: QixiaApplication
) : ViewModel() {
    val currentUser: StateFlow<CurrentLoginUser> = userRepository.getStreamById(application.currentLoginUserId!!)
        .map { CurrentLoginUser(listOf(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CurrentLoginUser()
        )
    var currentTemperature: Float by mutableFloatStateOf(24f)
    var currentHumidity: Float by mutableFloatStateOf(50f)
}