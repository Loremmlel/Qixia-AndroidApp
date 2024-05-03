package org.hinanawiyuzu.qixia.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.ui.theme.FontSize

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