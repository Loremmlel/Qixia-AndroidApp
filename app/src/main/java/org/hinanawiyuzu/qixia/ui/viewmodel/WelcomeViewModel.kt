package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.hinanawiyuzu.qixia.QixiaApplication
import org.hinanawiyuzu.qixia.data.entity.User
import org.hinanawiyuzu.qixia.data.repo.UserRepository
import org.hinanawiyuzu.qixia.ui.route.AppRoute
import org.hinanawiyuzu.qixia.ui.route.LoginRoute

class WelcomeViewModel(
    private val application: QixiaApplication,
    userRepository: UserRepository
) : ViewModel() {
    // 这里收集完毕后，一定要在@Composable函数里收集为StateFlow然后再传参使用。
    // 否则会出现什么都收集不到的情况。
    // 我想和协程、Flow的机制有关系。但是我现在完全搞不懂呢。
    val allUsers: StateFlow<AllUsers> =
        userRepository.getAllStream().map { AllUsers(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = AllUsers()
            )

    /**
     * 查询是否有已登录的用户。如果有，则设置application里的变量，返回true。否则返回false。
     */
    private fun getCurrentLoginUserId(allUsers: AllUsers): Boolean {
        var result = false
        allUsers.userList.forEach {
            if (it.loginState) {
                application.currentLoginUserId = it.id
                result = true
                return@forEach
            }
        }
        return result
    }

    /**
     * 决定要跳转到登录页面还是主页面
     */
    suspend fun navigateScreen(navController: NavController, allUsers: AllUsers) {
        delay(1000)
        var navigateRoute: String = LoginRoute.LoginScreen.name
        if (getCurrentLoginUserId(allUsers)) {
            navigateRoute = AppRoute.AppScreen.name
        }
        navController.navigate(navigateRoute) {
            // 弹出堆栈中的欢迎页面，防止用户按返回键再回到该页面。
            popUpTo("WelcomeScreen") {
                inclusive = true
            }
        }
    }
}

data class AllUsers(val userList: List<User> = emptyList())