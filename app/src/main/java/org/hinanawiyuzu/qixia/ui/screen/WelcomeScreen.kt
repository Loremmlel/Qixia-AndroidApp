package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.ui.AppViewModelProvider
import org.hinanawiyuzu.qixia.ui.route.AppRoute
import org.hinanawiyuzu.qixia.ui.route.LoginRoute
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.viewmodel.WelcomeViewModel
import org.hinanawiyuzu.qixia.utils.advancedShadow

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