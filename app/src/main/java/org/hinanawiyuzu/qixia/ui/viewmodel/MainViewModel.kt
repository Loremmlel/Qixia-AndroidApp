package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.lifecycle.*
import androidx.navigation.*
import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.*
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.data.repo.*

class MainViewModel(
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

    fun onBannerClicked(
        id: Int,
        navController: NavController,
    ) {

    }
}

data class CurrentLoginUser(
    val user: List<User> = emptyList()
)