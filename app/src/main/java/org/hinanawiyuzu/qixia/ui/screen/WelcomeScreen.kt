package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.*
import androidx.navigation.*
import androidx.navigation.compose.*
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.ui.*
import org.hinanawiyuzu.qixia.ui.route.*
import org.hinanawiyuzu.qixia.ui.theme.*
import org.hinanawiyuzu.qixia.ui.viewmodel.*
import org.hinanawiyuzu.qixia.utils.*

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = viewModel(factory = AppViewModelProvider.factory),
    navController: NavHostController = rememberNavController()
) {
    // 收集!
    val allUsers by viewModel.allUsers.collectAsState()
    NavHost(
        navController = navController,
        startDestination = "WelcomeScreen"
    ) {
        //TODO: 这里应当加验证是否登录的逻辑。如果已登录，直接跳转到MainScreen。
        composable(route = "WelcomeScreen") {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Logo(modifier = modifier.weight(3.5f))
                Title(modifier = modifier.weight(2f))
            }
            LaunchedEffect(allUsers) {
                // 为什么把delay放到这里就可以
                // 而把viewModel.navigateScreen设置为suspend然后调用delay就不行呢。
                // 更新：把allUsers设置为key，让LaunchedEffect根据allUsers的变化来判断是否要执行就可以了。
                // 传参！
                viewModel.navigateScreen(navController, allUsers)
            }
        }
        composable(route = LoginRoute.LoginScreen.name) {
            LoginScreen()
        }
        composable(route = AppRoute.AppScreen.name) {
            AppScreen()
        }
    }
}

@Composable
@Stable
private fun Logo(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .advancedShadow(
                    alpha = 0.2f,
                    cornersRadius = 10.dp,
                    shadowBlurRadius = 20.dp,
                    offsetY = (-10).dp
                )
                .clip(shape = RoundedCornerShape(50.dp)),
            painter = painterResource(id = R.drawable.welcome_screen_logo_bottom_rec),
            contentDescription = null
        )
        Image(
            modifier = Modifier
                .advancedShadow(
                    alpha = 1f,
                    cornersRadius = 10.dp,
                    shadowBlurRadius = 5.dp,
                    offsetY = 5.dp
                )
                .clip(shape = RoundedCornerShape(15.dp)),
            painter = painterResource(id = R.drawable.welcome_screen_logo_middle_rec),
            contentDescription = null
        )
        Image(
            painter = painterResource(id = R.drawable.welcome_screen_logo_cross),
            contentDescription = null
        )
        Image(
            modifier = Modifier
                .graphicsLayer(rotationZ = 90f),
            painter = painterResource(id = R.drawable.welcome_screen_logo_cross),
            contentDescription = null
        )
    }
}

@Composable
@Stable
private fun Title(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier,
            painter = painterResource(id = R.drawable.welcome_screen_title),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    QixiaTheme {
        WelcomeScreen()
    }
}