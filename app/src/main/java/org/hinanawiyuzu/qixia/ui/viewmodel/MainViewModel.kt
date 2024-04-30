package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.hinanawiyuzu.qixia.QixiaApplication
import org.hinanawiyuzu.qixia.data.entity.User
import org.hinanawiyuzu.qixia.data.repo.UserRepository
import org.hinanawiyuzu.qixia.ui.route.MainRoute

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
        navController.navigate("${MainRoute.HealthMessageScreen.name}/$id")
    }
}

data class CurrentLoginUser(
    val user: List<User> = emptyList()
)