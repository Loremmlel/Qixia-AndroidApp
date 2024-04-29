package org.hinanawiyuzu.qixia.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.ui.theme.*

@Composable
fun Fold(
    modifier: Modifier = Modifier,
    text: String,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationDegree by animateFloatAsState(targetValue = if (expanded) 180f else 0f, label = "图标旋转动画")
    val containerColor = Color(0x70FFFFFF)
    val borderGradient = Brush.linearGradient(colors = listOf(Color(0xFFD3EEE0), Color(0xFFF1FFF8)))
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(containerColor)
            .border(width = 0.5.dp, borderGradient, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = FontSize.veryLargeSize
            )
            Icon(
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .rotate(rotationDegree),
                painter = painterResource(id = R.drawable.fold_corner_mark),
                contentDescription = "展开信息"
            )
        }
        AnimatedVisibility(
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top),
            visible = expanded
        ) {
            content()
        }
    }
}