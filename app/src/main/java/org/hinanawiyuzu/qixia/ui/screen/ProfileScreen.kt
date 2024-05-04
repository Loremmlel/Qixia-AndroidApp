package org.hinanawiyuzu.qixia.ui.screen

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.ui.AppViewModelProvider
import org.hinanawiyuzu.qixia.ui.route.ProfileRoute
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.theme.MyColor
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.theme.secondary_color
import org.hinanawiyuzu.qixia.ui.viewmodel.ProfileViewModel
import org.hinanawiyuzu.qixia.utils.showLongToast
import org.hinanawiyuzu.qixia.utils.toBitmap

@Composable
fun ProfileScreen(
  modifier: Modifier = Modifier,
  changeBottomBarVisibility: (Boolean) -> Unit,
  viewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.factory),
  navController: NavHostController = rememberNavController()
) {
  val currentLoginUser by viewModel.currentUser.collectAsState()
  NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = ProfileRoute.ProfileScreen.name
  ) {
    composable(
      route = ProfileRoute.ProfileScreen.name,
      exitTransition = {
        slideOutHorizontally(animationSpec = tween(500), targetOffsetX = { -it })
      },
      enterTransition = {
        slideInHorizontally(animationSpec = tween(500), initialOffsetX = { it })
      },
      // pop可以控制用户按返回键的动画，即NavigateUp,同Navigate区分开来。
      popEnterTransition = {
        slideInHorizontally(animationSpec = tween(500), initialOffsetX = { -it })
      }
    ) {
      changeBottomBarVisibility(true)
      Column(
        modifier = Modifier
          .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        if (currentLoginUser.user.isNotEmpty()) {
          TopPersonalInformation(
            modifier = Modifier
              .padding(horizontal = 20.dp)
              .fillMaxWidth(),
            profilePhotoUri = null,
            name = currentLoginUser.user[0].name,
            phone = currentLoginUser.user[0].phone,
          ) {

          }
        }
        VIPArea(
          modifier = Modifier
            .fillMaxWidth()
            .height(75.dp),
          visible = viewModel.isVIPVisible,
          onBecomeVIPClicked = { /*TODO*/ },
          onCloseClicked = viewModel::closeVIP
        )
        FeatureList(
          modifier = Modifier.fillMaxWidth(),
          iconsRes = listOf(
            R.drawable.profile_notification,
            R.drawable.profile_customer_service,
            R.drawable.profile_record_export,
            R.drawable.profile_settings,
            R.drawable.profile_about_us,
            R.drawable.profile_help_and_feedback
          ),
          texts = listOf("通知", "在线客服", "用药记录导出", "设置", "关于我们", "帮助与反馈"),
          onFeaturesClicked = listOf(
            { viewModel.onNotificationClicked(navController) },
            { viewModel.onCustomerServiceClicked(navController) },
            { viewModel.onRecordExportClicked(navController) },
            { viewModel.onSettingClicked(navController) },
            { viewModel.onAboutUsClicked(navController) },
            { viewModel.onHelpAndFeedbackClicked(navController) }
          )
        )
        LogoutButton(
          modifier = Modifier
            .padding(horizontal = 10.dp)
            .height(45.dp)
            .fillMaxWidth()
        ) {
          viewModel.onLogoutClicked()
        }
      }
    }
  }
}

@Composable
private fun TopPersonalInformation(
  modifier: Modifier = Modifier,
  profilePhotoUri: Uri? = null,
  name: String?,
  phone: String?,
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
        .height(90.dp)
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
          modifier = Modifier
            .clip(CircleShape)
            .fillMaxSize(),
          bitmap = profilePhotoBitmap?.asImageBitmap() ?: ImageBitmap(1, 1),
          contentDescription = "头像",
          contentScale = ContentScale.FillWidth,
        )
      }
    }
    // 个人信息（如名称、电话的前三位和后四位，还有APP“陪伴”了多少天）
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = name ?: "",
        fontSize = FontSize.largeSize,
        fontWeight = FontWeight.ExtraBold
      )
      Text(
        text = phone?.let { "${phone.substring(0..2)}****${phone.substring(phone.length - 4)}" } ?: "",
        fontSize = FontSize.tinySize,
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
  visible: Boolean,
  onBecomeVIPClicked: () -> Unit,
  onCloseClicked: () -> Unit
) {
  AnimatedVisibility(
    modifier = modifier,
    visible = visible,
    exit = shrinkVertically(animationSpec = tween(1000), shrinkTowards = Alignment.Top),
  ) {
    val backgroundGradient = Brush.verticalGradient(listOf(Color(0xFFFDFEFE), Color(0xFFFFF2AD)))
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Row(
        modifier = Modifier
          .clip(RoundedCornerShape(percent = 20))
          .fillMaxSize()
          .background(brush = backgroundGradient),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        val vipColor1 = Color(0xFFF59A19)
        val vipColor2 = Color(0xFFE87400)
        Image(painter = painterResource(id = R.drawable.vip), contentDescription = null)
        Text(text = "成为VIP享更多权益", color = vipColor1)
        Button(
          modifier = Modifier
            .clip(RoundedCornerShape(percent = 50))
            .fillMaxHeight(0.5f)
            .background(MyColor.vipBrush),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent
          ),
          onClick = onBecomeVIPClicked
        ) {
          Text(text = "成为会员", color = vipColor2)
        }
      }
      IconButton(
        modifier = Modifier
          .scale(0.8f)
          .offset(x = 15.dp, y = (-15).dp)
          .align(Alignment.TopEnd)
          .clip(CircleShape),
        onClick = { onCloseClicked() }
      ) {
        Icon(
          painter = painterResource(id = R.drawable.baseline_close_24),
          contentDescription = "关闭VIP区域"
        )
      }
    }
  }
}

@Composable
private fun FeatureList(
  modifier: Modifier = Modifier,
  iconsRes: List<Int>,
  texts: List<String>,
  onFeaturesClicked: List<() -> Unit>
) {
  val context = LocalContext.current
  if (iconsRes.size != texts.size || texts.size != onFeaturesClicked.size) {
    Log.e("FeatureList", "iconsRes.size != texts.size || texts.size != onFeaturesClicked.size")
    showLongToast(context, "应用程序出现异常，详情见日志")
    return
  }
  val size = iconsRes.size
  Column(
    modifier = modifier
      .padding(vertical = 10.dp)
      .clip(RoundedCornerShape(percent = 5))
      .background(MyColor.lightGreenCardGradient),
  ) {
    repeat(size) {
      Row(
        modifier = Modifier
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            onClick = onFeaturesClicked[it],
            indication = null
          )
          .padding(horizontal = 15.dp)
          .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          modifier = Modifier.padding(vertical = 15.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
          Image(
            painter = painterResource(id = iconsRes[it]),
            contentDescription = texts[it]
          )
          Text(
            text = texts[it],
            fontSize = FontSize.bigSize
          )
        }
        IconButton(
          modifier = Modifier,
          onClick = onFeaturesClicked[it]
        ) {
          Icon(
            painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
            contentDescription = texts[it],
            tint = Color.LightGray
          )
        }
      }
      if (it != size - 1) {
        Spacer(
          modifier = modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0xFFEEEEEE))
        )
      }
    }
  }
}

@Composable
private fun LogoutButton(
  modifier: Modifier = Modifier,
  onLogoutClicked: () -> Unit
) {
  Column(
    modifier = modifier
      .clickable { onLogoutClicked() }
      .clip(RoundedCornerShape(percent = 50))
      .fillMaxWidth()
      .background(MyColor.deepGreenButtonGradient),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = "退出登录",
      color = Color.White,
      fontSize = FontSize.bigSize,
      fontWeight = FontWeight.Bold
    )
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
