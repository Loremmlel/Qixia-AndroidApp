package org.hinanawiyuzu.qixia.ui.screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.ui.AppViewModelProvider
import org.hinanawiyuzu.qixia.ui.theme.FontSize
import org.hinanawiyuzu.qixia.ui.viewmodel.VIPSetMeal
import org.hinanawiyuzu.qixia.ui.viewmodel.VIPVersion
import org.hinanawiyuzu.qixia.ui.viewmodel.VIPViewModel
import org.hinanawiyuzu.qixia.utils.toBitmap

@Composable
fun VIPScreen(
  modifier: Modifier = Modifier,
  viewModel: VIPViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.factory),
  navController: NavHostController,
) {
  val currentLoginUser by viewModel.currentUser.collectAsState()
  Column(
    modifier = modifier.fillMaxSize()
  ) {
    val topGradient = Brush.linearGradient(listOf(Color(0xFFEEFFF7), Color(0xFFE4FDF1), Color(0xFF8AD4AF)))
    Box(
      modifier = Modifier
        .background(topGradient)
        .fillMaxHeight(0.2f)
    ) {
      TopBar(
        modifier = Modifier
          .align(Alignment.TopCenter)
          .padding(top = 10.dp)
          .fillMaxWidth(),
        onBackClicked = { navController.popBackStack() },
        onCustomerServiceClicked = { }
      )
      if (currentLoginUser.user.isNotEmpty()) {
        UserInfo(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(start = 20.dp, bottom = 20.dp)
            .fillMaxHeight(0.45f)
            .fillMaxWidth(0.5f),
          profilePhotoUri = null,
          phone = currentLoginUser.user[0].phone
        )
      }
      Image(
        modifier = Modifier.align(Alignment.BottomEnd),
        painter = painterResource(id = R.drawable.vip_diamond),
        contentDescription = null
      )
    }
    SelectCard(
      modifier = Modifier
        .fillMaxWidth()
        .height(60.dp),
      currentVIPVersion = viewModel.currentVIPVersion,
      onBoxClicked = { viewModel.currentVIPVersion = VIPVersion.Box },
      onPersonalClicked = { viewModel.currentVIPVersion = VIPVersion.Personal }
    )
    Column(
      modifier = Modifier
        .background(Color.White)
        .padding(vertical = 15.dp)
    ) {
      VIPInterests(
        currentVIPVersion = viewModel.currentVIPVersion
      )
      VIPSetMeal(
        modifier = Modifier
          .padding(top = 20.dp)
          .height(200.dp),
        currentVIPVersion = viewModel.currentVIPVersion,
        currentVIPSetMeal = viewModel.currentVIPSetMeal,
        onCurrentVIPSetMealChanged = { viewModel.onVIPSetMealChanged(it) }
      )
      BottomButton(
        modifier = Modifier
          .padding(top = 20.dp)
          .fillMaxWidth()
          .height(65.dp),
        currentVIPSelection = viewModel.currentVIPVersion to viewModel.currentVIPSetMeal
      ) {}
      VIPProtocol(
        modifier = Modifier
          .align(Alignment.CenterHorizontally)
          .offset(y = 15.dp)
      ) {}
      Spacer(
        modifier = Modifier
          .weight(1f)
          .background(Color.White)
      )
    }
  }
}

@Composable
private fun TopBar(
  modifier: Modifier = Modifier,
  onBackClicked: () -> Unit,
  onCustomerServiceClicked: () -> Unit
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    IconButton(onClick = onBackClicked) {
      Icon(
        painter = painterResource(id = R.drawable.left_arrow),
        contentDescription = "返回"
      )
    }
    Text(
      text = "会员中心",
      fontSize = FontSize.veryLargeSize,
      fontWeight = FontWeight.Bold
    )
    IconButton(onClick = onCustomerServiceClicked) {
      Icon(
        painter = painterResource(id = R.drawable.vip_customer_service),
        contentDescription = "返回",
        tint = Color.White
      )
    }
  }
}

@Composable
private fun UserInfo(
  modifier: Modifier = Modifier,
  profilePhotoUri: Uri? = null,
  phone: String?,
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
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Box(
      modifier = Modifier
        .clip(CircleShape)
        .height(80.dp)
        .aspectRatio(1f)
        .background(color = Color(0xFFF4F8F2)),
    ) {
      Image(
        modifier = Modifier
          .clip(CircleShape)
          .fillMaxSize(),
        bitmap = profilePhotoBitmap?.asImageBitmap() ?: ImageBitmap(1, 1),
        contentDescription = "头像",
        contentScale = ContentScale.FillBounds,
      )
    }
    val phoneColor = Color(0xFF799E28)
    val fontColor = Color(0xFF3E1D04)
    Column(
      modifier = Modifier.fillMaxHeight(),
      verticalArrangement = Arrangement.SpaceBetween,
    ) {
      Text(
        text = "尾号${phone?.takeLast(4)}",
        color = phoneColor,
        fontSize = FontSize.bigSize,
      )
      Text(
        modifier = Modifier.offset(x = (-5).dp),
        text = "未开通",
        color = fontColor,
        fontSize = FontSize.smallSize
      )
    }
  }
}

@Composable
private fun SelectCard(
  modifier: Modifier = Modifier,
  currentVIPVersion: VIPVersion,
  onBoxClicked: () -> Unit,
  onPersonalClicked: () -> Unit
) {
  val boxGradient = Brush.linearGradient(
    listOf(
      Color(0xFFDEFDEE), Color(0xFFD6F2E4), Color(0xFFD6F2E4), Color.White
    )
  )
  val personalGradient = Brush.linearGradient(
    listOf(
      Color(0xFF93E6BC), Color(0xFFA2DFC0), Color.White
    )
  )
  val roundedCornerShapePercent = 25
  val imageScale = 2.5f
  Box(
    modifier = modifier
  ) {
    Row(
      modifier = Modifier
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null
        ) { onBoxClicked() }
        .clip(RoundedCornerShape(percent = roundedCornerShapePercent))
        .zIndex(if (currentVIPVersion == VIPVersion.Box) 1f else 0f)
        .align(Alignment.CenterStart)
        .fillMaxWidth(0.55f)
        .fillMaxHeight()
        .background(boxGradient),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      if (currentVIPVersion == VIPVersion.Box) {
        Image(
          modifier = Modifier
            .scale(imageScale)
            .padding(end = 5.dp),
          painter = painterResource(id = R.drawable.vip_crown),
          contentDescription = null
        )
      }
      Text(
        text = "药箱版VIP",
        fontSize = FontSize.mediumLargeSize,
        fontWeight = FontWeight.ExtraBold
      )
    }
    Row(
      modifier = Modifier
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null
        ) { onPersonalClicked() }
        .clip(RoundedCornerShape(percent = roundedCornerShapePercent))
        .zIndex(if (currentVIPVersion == VIPVersion.Personal) 1f else 0f)
        .align(Alignment.CenterEnd)
        .fillMaxWidth(0.55f)
        .fillMaxHeight()
        .background(personalGradient),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      if (currentVIPVersion == VIPVersion.Personal) {
        Image(
          modifier = Modifier
            .scale(imageScale)
            .padding(end = 5.dp),
          painter = painterResource(id = R.drawable.vip_crown),
          contentDescription = null,
        )
      }
      Text(
        text = "个人版VIP",
        fontSize = FontSize.mediumLargeSize,
        fontWeight = FontWeight.ExtraBold
      )
    }
  }
}

@Composable
private fun VIPInterests(
  modifier: Modifier = Modifier,
  currentVIPVersion: VIPVersion
) {
  val firstLineIconsRes = listOf(
    R.drawable.vip_pill,
    R.drawable.vip_health_gift,
    R.drawable.vip_interrogation,
    R.drawable.vip_contact_person
  )
  val secondLineIconsRes = listOf(
    R.drawable.vip_analyze,
    R.drawable.vip_multible,
    R.drawable.vip_credit_exchange,
    R.drawable.vip_customized_scheme
  )
  val boxFirstLineTexts = listOf("专享立减", "健康礼品", "无限问诊", "5位联系人")
  val boxSecondLineTexts = listOf("无限分析", "多人共享", "积分兑换", "定制方案")
  val personalFirstLineTexts = listOf("专享立减", "体检、复查提醒", "多次免费问诊", "3位联系人")
  val personalSecondLineTexts = listOf("深度分析", "多人共享", "积分兑换", "定制语音包")
  val boxGradient = Brush.linearGradient(
    listOf(Color(0xFF9CE0BE), Color(0xFFFDFEFE))
  )
  val boxSize = 60.dp
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Text(
      modifier = Modifier.padding(start = 20.dp, bottom = 10.dp),
      text = "会员专属权益",
      fontSize = FontSize.largeSize,
      fontWeight = FontWeight.ExtraBold
    )
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      repeat(4) {
        Column(
          modifier = Modifier,
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .size(boxSize)
              .aspectRatio(1f)
              .background(boxGradient),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = painterResource(id = firstLineIconsRes[it]),
              contentDescription =
              if (currentVIPVersion == VIPVersion.Box) boxFirstLineTexts[it] else personalFirstLineTexts[it],
            )
          }
          Text(
            text = if (currentVIPVersion == VIPVersion.Box) boxFirstLineTexts[it] else personalFirstLineTexts[it],
            fontSize = FontSize.tinySize,
            fontWeight = FontWeight.ExtraBold
          )
        }
      }
    }
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      repeat(4) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .size(boxSize)
              .aspectRatio(1f)
              .background(boxGradient),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = painterResource(id = secondLineIconsRes[it]),
              contentDescription =
              if (currentVIPVersion == VIPVersion.Box) boxSecondLineTexts[it] else personalSecondLineTexts[it],
            )
          }
          Text(
            text = if (currentVIPVersion == VIPVersion.Box) boxSecondLineTexts[it] else personalSecondLineTexts[it],
            fontSize = FontSize.tinySize,
            fontWeight = FontWeight.ExtraBold
          )
        }
      }
    }
  }
}

@Composable
private fun VIPSetMeal(
  modifier: Modifier = Modifier,
  currentVIPVersion: VIPVersion,
  currentVIPSetMeal: VIPSetMeal,
  onCurrentVIPSetMealChanged: (VIPSetMeal) -> Unit
) {
  val vipDiscountPrice = when (currentVIPVersion) {
    VIPVersion.Box -> listOf(78, 38, 12, 5)
    VIPVersion.Personal -> listOf(250, 120, 38, 15)
  }
  val vipOriginalPrice = when (currentVIPVersion) {
    VIPVersion.Box -> listOf(280, 120, 38, 15)
    VIPVersion.Personal -> listOf(528, 220, 68, 25)
  }
  val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
  val cardWidthDp = screenWidthDp / 4.5f

  @Composable
  fun VIPSetMealCard(
    modifier: Modifier = Modifier,
    vipSetMeal: VIPSetMeal,
    tagline: String,
    discountPrice: Int,
    originalPrice: Int,
    onCurrentVIPSetMealChanged: (VIPSetMeal) -> Unit
  ) {
    val text = when (vipSetMeal) {
      VIPSetMeal.Lifelong -> "终身VIP"
      VIPSetMeal.Yearly -> "年卡"
      VIPSetMeal.Quarterly -> "季卡"
      VIPSetMeal.Monthly -> "月卡"
    }
    val borderColor = Color(0xFF338A5F)
    val selectedGradient = Brush.verticalGradient(listOf(Color(0xFFB8E3CD), Color(0xFFFDFEFE)))
    val discountColor = Color(244, 197, 132)
    val vipFontColor = Color(0xFF7B4D14)
    val priceColor = Color(0xFFEC0000)
    Box(
      modifier = modifier
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null
        ) { onCurrentVIPSetMealChanged(vipSetMeal) }
    ) {
      val columnModifier = Modifier
        .shadow(elevation = if (currentVIPSetMeal == vipSetMeal) 5.dp else 2.dp, shape = RoundedCornerShape(30))
        .clip(RoundedCornerShape(30))
        .align(Alignment.Center)
        .fillMaxSize(0.9f)
        .border(1.dp, borderColor, RoundedCornerShape(30))
      Column(
        modifier = columnModifier.then(
          if (currentVIPSetMeal == vipSetMeal) Modifier.background(selectedGradient)
          else Modifier.background(Color.White)
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = text,
          fontSize = FontSize.largeSize,
          fontWeight = FontWeight.ExtraBold
        )
        Row(verticalAlignment = Alignment.Bottom) {
          Text(
            text = "¥",
            color = priceColor,
            fontSize = FontSize.tinySize,
            fontWeight = FontWeight.ExtraBold
          )
          Text(
            text = "$discountPrice",
            color = priceColor,
            fontSize = FontSize.veryLargeSize,
            fontWeight = FontWeight.ExtraBold
          )
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        Text(
          text = "原价$originalPrice",
          textDecoration = TextDecoration.LineThrough,
          color = Color.Gray,
          fontSize = FontSize.tinySize
        )
      }
      if (currentVIPSetMeal == vipSetMeal) {
        Image(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .offset(y = 20.dp),
          painter = painterResource(id = R.drawable.vip_medal),
          contentDescription = null
        )
      }
      Row(
        modifier = Modifier
          .clip(RoundedCornerShape(topStartPercent = 40, bottomEndPercent = 40))
          .align(Alignment.TopStart)
          .background(discountColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          modifier = Modifier.padding(5.dp),
          text = tagline,
          color = vipFontColor,
          fontSize = FontSize.veryTineSize,
          fontWeight = FontWeight.ExtraBold
        )
      }
    }
  }
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Text(
      modifier = Modifier.padding(start = 20.dp, bottom = 10.dp),
      text = "选择会员套餐",
      fontSize = FontSize.largeSize,
      fontWeight = FontWeight.ExtraBold
    )
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      VIPSetMealCard(
        modifier = Modifier.width(cardWidthDp * 1.5f),
        vipSetMeal = VIPSetMeal.Lifelong,
        tagline = "尊享终身权益",
        discountPrice = vipDiscountPrice[0],
        originalPrice = vipOriginalPrice[0],
        onCurrentVIPSetMealChanged = onCurrentVIPSetMealChanged
      )
      VIPSetMealCard(
        modifier = Modifier.width(cardWidthDp),
        vipSetMeal = VIPSetMeal.Yearly,
        tagline = if (currentVIPVersion == VIPVersion.Box) "低至0.1/天" else "低至0.3/天",
        discountPrice = vipDiscountPrice[1],
        originalPrice = vipOriginalPrice[1],
        onCurrentVIPSetMealChanged = onCurrentVIPSetMealChanged
      )
      VIPSetMealCard(
        modifier = Modifier.width(cardWidthDp),
        vipSetMeal = VIPSetMeal.Quarterly,
        tagline = if (currentVIPVersion == VIPVersion.Box) "低至0.13/天" else "低至0.4/天",
        discountPrice = vipDiscountPrice[2],
        originalPrice = vipOriginalPrice[2],
        onCurrentVIPSetMealChanged = onCurrentVIPSetMealChanged
      )
      VIPSetMealCard(
        modifier = Modifier.width(cardWidthDp),
        vipSetMeal = VIPSetMeal.Monthly,
        tagline = if (currentVIPVersion == VIPVersion.Box) "低至0.17/天" else "低至0.5/天",
        discountPrice = vipDiscountPrice[3],
        originalPrice = vipOriginalPrice[3],
        onCurrentVIPSetMealChanged = onCurrentVIPSetMealChanged
      )
    }
  }
}

@Composable
private fun BottomButton(
  modifier: Modifier = Modifier,
  currentVIPSelection: Pair<VIPVersion, VIPSetMeal>,
  onButtonClicked: () -> Unit
) {
  val price = when (currentVIPSelection) {
    VIPVersion.Box to VIPSetMeal.Lifelong -> 78
    VIPVersion.Box to VIPSetMeal.Yearly -> 38
    VIPVersion.Box to VIPSetMeal.Quarterly -> 12
    VIPVersion.Box to VIPSetMeal.Monthly -> 5
    VIPVersion.Personal to VIPSetMeal.Lifelong -> 250
    VIPVersion.Personal to VIPSetMeal.Yearly -> 120
    VIPVersion.Personal to VIPSetMeal.Quarterly -> 38
    VIPVersion.Personal to VIPSetMeal.Monthly -> 15
    else -> 233
  }
  val borderGradient = Brush.linearGradient(listOf(Color(0xFFD6A26D), Color(0xFFF6CEB5)))
  val containerGradient = Brush.linearGradient(listOf(Color(0xFFF7DAB9), Color(0xFFF6A02C)))
  val innerButtonContainerGradient = Brush.linearGradient(listOf(Color(0xFFFECE98), Color(0xFFF59A19)))
  val containerShape = RoundedCornerShape(40)
  val innerButtonContainerShape = RoundedCornerShape(30)
  val fontColor = Color(0xFFEC0000)
  Row(
    modifier = modifier
      .shadow(5.dp, containerShape)
      .clip(containerShape)
      .background(containerGradient)
      .border(1.dp, borderGradient, containerShape),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceAround
  ) {
    Row(
      verticalAlignment = Alignment.Bottom
    ) {
      Text(
        text = "仅需:  ¥",
        fontSize = FontSize.tinySize,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFDA1414)
      )
      Text(
        text = "$price",
        fontSize = 40.sp,
        fontWeight = FontWeight.Black,
        color = fontColor
      )
    }
    Row(
      modifier = Modifier
        .clickable { onButtonClicked() }
        .clip(innerButtonContainerShape)
        .shadow(6.dp, innerButtonContainerShape)
        .background(innerButtonContainerGradient)
        .padding(horizontal = 15.dp, vertical = 5.dp)
        .fillMaxHeight(0.7f),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Text(
        text = "立即开通",
        fontSize = FontSize.bigSize
      )
    }
  }
}

@Composable
private fun VIPProtocol(
  modifier: Modifier = Modifier,
  onProtocolClicked: () -> Unit
) {
  val vipColor = Color(245, 203, 148)
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = "开通前请阅读",
      fontSize = FontSize.tinySize,
      color = Color.LightGray
    )
    Text(
      modifier = Modifier.clickable { onProtocolClicked() },
      text = "《祺匣会员协议》",
      color = vipColor,
      fontSize = FontSize.tinySize,
    )
  }
}