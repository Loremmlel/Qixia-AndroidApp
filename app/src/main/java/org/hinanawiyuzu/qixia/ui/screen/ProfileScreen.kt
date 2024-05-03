package org.hinanawiyuzu.qixia.ui.screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.theme.secondary_color
import org.hinanawiyuzu.qixia.ui.viewmodel.ProfileViewModel
import org.hinanawiyuzu.qixia.utils.toBitmap

@Composable
fun ProfileScreen(
  modifier: Modifier = Modifier,
  viewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

}

@Composable
private fun TopPersonalInformation(
  modifier: Modifier = Modifier,
  profilePhotoUri: Uri? = null,
  name: String,
  phone: String,
  days: Int = 20,
  onRightArrowClick: () -> Unit,
) {
  val context = LocalContext.current
  var profilePhotoBitmap: Bitmap? by remember { mutableStateOf(null) }
  LaunchedEffect(profilePhotoUri) {
    profilePhotoBitmap = async(Dispatchers.IO) {
      profilePhotoUri?.toBitmap(context) ?: R.drawable.default_profile_photo_female.toBitmap(context)
    }.await()
  }
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    // 头像框
    val borderColor = Color(0xFF338A5F)
    Box(
      modifier = Modifier
        .clip(CircleShape)
        .height(75.dp)
        .aspectRatio(1f)
        .border(width = 2.dp, color = borderColor, shape = CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Box(
        modifier = Modifier
          .clip(CircleShape)
          .aspectRatio(1f)
          .fillMaxSize()
          .padding(10.dp)
          .background(color = Color(0xFFF4F8F2)),
        contentAlignment = Alignment.BottomCenter
      ) {
        Image(
          bitmap = profilePhotoBitmap?.asImageBitmap() ?: ImageBitmap(1, 1),
          contentDescription = "头像",
          contentScale = ContentScale.Crop,
        )
      }
    }
    // 个人信息（如名称、电话的前三位和后四位，还有APP“陪伴”了多少天）
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = name,
        fontSize = FontSize.largeSize,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "${phone.substring(0..2)}****${phone.substring(phone.length - 4)}",
        fontSize = FontSize.smallSize,
        fontWeight = FontWeight.Light,
        color = Color.Gray
      )
      Text(
        text = "祺匣已陪伴您走过${days}天",
        color = secondary_color
      )
    }
    // 灰色箭头，进入个人信息展示和编辑界面
    Icon(
      modifier = Modifier.clickable { onRightArrowClick() },
      painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
      contentDescription = "进入个人信息展示和编辑界面",
      tint = Color.Gray
    )
  }
}

@Composable
private fun VIPArea(
  modifier: Modifier = Modifier,
  onBecomeVIPClick: () -> Unit
) {
  var visible by remember { mutableStateOf(true) }
  AnimatedVisibility(
    modifier = modifier,
    visible = visible,
    enter = expandIn(animationSpec = tween(1000), expandFrom = Alignment.TopStart),
    exit = shrinkOut(animationSpec = tween(1000), shrinkTowards = Alignment.TopStart)
  ) {
    val backgroundGradient = Brush.radialGradient(listOf(Color.White, Color(0xFFFDFEFE), Color(0xFFFFF2AD)))
    Row(
      modifier = Modifier
        .clip(RoundedCornerShape(percent = 10))
        .fillMaxSize()
        .background(brush = backgroundGradient),
    ) {

    }
  }
}

@Preview
@Composable
fun TopPersonalInformationPreview() {
  QixiaTheme {
    TopPersonalInformation(
      modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
      profilePhotoUri = null,
      name = "张三",
      phone = "12345678901",
      days = 20,
      onRightArrowClick = {}
    )
  }
}
