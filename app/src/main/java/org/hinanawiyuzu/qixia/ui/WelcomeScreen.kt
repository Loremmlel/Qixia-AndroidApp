package org.hinanawiyuzu.qixia.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.utils.advancedShadow

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "WelcomeScreen"
    ) {
        // TODO: 这里应当加验证是否登录的逻辑。如果登录，直接跳转到MainScreen。
        composable(route = "WelcomeScreen") {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Logo(modifier = modifier.weight(3.5f))
                Title(modifier = modifier.weight(2f))
            }
            LaunchedEffect(Unit) {
                delay(1000) //暂停1s
                navController.navigate("LoginScreen")
            }
        }
        composable(route = "LoginScreen") {
            LoginScreen()
        }
        composable(route = "MainScreen") {

        }
    }
}

@Composable
@Stable
fun Logo(
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
            contentDescription = null)
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
            contentDescription = null)
        Image(
            painter = painterResource(id = R.drawable.welcome_screen_logo_cross),
            contentDescription = null)
        Image(
            modifier = Modifier
                .graphicsLayer(rotationZ = 90f),
            painter = painterResource(id = R.drawable.welcome_screen_logo_cross),
            contentDescription = null)
    }
}

@Composable
@Stable
fun Title(
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