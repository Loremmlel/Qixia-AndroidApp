package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.hinanawiyuzu.qixia.components.BlurredBackground

@Composable
fun NewMedicineScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    val selectorHeight: Dp = 35.dp
    val screenWidthDp: Dp = LocalConfiguration.current.screenWidthDp.dp
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        BlurredBackground()
    }
}