package org.hinanawiyuzu.qixia.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.hinanawiyuzu.qixia.ui.viewmodel.BoxViewModel

@Composable
fun BoxScreen(
    modifier: Modifier = Modifier,
    viewModel: BoxViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavController = rememberNavController(),
) {

}

/**
 * 温度显示
 */
@Composable
private fun AnimateTempDisplay(
    modifier: Modifier = Modifier,
    currentTemperature: Float = 24f
) {
    val greenTint = Color(0xFF00BA61)
    val minTemperature = 16f
    val maxTemperature = 32f
    val normalizedTemperature = (currentTemperature - minTemperature) / (maxTemperature - minTemperature)
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

    }
}