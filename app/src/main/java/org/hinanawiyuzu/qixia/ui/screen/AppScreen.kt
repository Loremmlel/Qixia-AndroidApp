package org.hinanawiyuzu.qixia.ui.screen

import android.content.res.Resources
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.hinanawiyuzu.qixia.R
import org.hinanawiyuzu.qixia.components.BlurredBackground
import org.hinanawiyuzu.qixia.components.MyIconButton
import org.hinanawiyuzu.qixia.ui.theme.MyColor.bottomAnimatedCircleGradient
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme
import org.hinanawiyuzu.qixia.ui.viewmodel.AppViewModel
import org.hinanawiyuzu.qixia.utils.AppScreenState

// 底部导航栏动画持续时间(ms)
const val animationTime: Int = 300

@Composable
fun AppScreen(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = viewModel()
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        BlurredBackground()
        when (viewModel.appScreenState) {
            AppScreenState.Main -> {}
            AppScreenState.Box -> {}
            AppScreenState.Remind -> {
                RemindScreen(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        // 正好覆盖到下方导航栏
                        // 按比例布局的好处（不是）
                        // 唉卧槽，那我干什么整screenWidthDp,直接fillMaxHeight,fillMaxWidth + 比例不就好了吗
                        // 唉，我真傻，真的。
                        .fillMaxHeight(0.9167f)
                )
            }

            AppScreenState.Record -> {}
            AppScreenState.Profile -> {}
        }
        AnimatedBottomAppBar(
            onBottomBarItemClicked = viewModel::onBottomBarItemClicked,
            appScreenState = viewModel.appScreenState
        )
    }
}

@Composable
private fun AnimatedBottomAppBar(
    modifier: Modifier = Modifier,
    appScreenState: AppScreenState,
    onBottomBarItemClicked: (Int) -> Unit
) {
    val screenWidthPx = Resources.getSystem().displayMetrics.widthPixels
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    var itemWidthDp by remember { mutableFloatStateOf(0f) } //一个导航按钮的宽度dp值
    var spaceWidthDp by remember { mutableFloatStateOf(0f) } //导航按钮之间空格的宽度dp值
    @DrawableRes val currentImageRes = when (appScreenState) {
        AppScreenState.Main -> R.drawable.mybottom_app_bar_status1
        AppScreenState.Box -> R.drawable.mybottom_app_bar_status2
        AppScreenState.Remind -> R.drawable.mybottom_app_bar_status3
        AppScreenState.Record -> R.drawable.mybottom_app_bar_status4
        AppScreenState.Profile -> R.drawable.mybottom_app_bar_status5
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height((screenHeightDp / 9).dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Crossfade(
            targetState = currentImageRes,
            animationSpec = tween(animationTime),
            label = "底部导航栏淡入淡出动画"
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((screenHeightDp / 12).dp),
                painter = painterResource(id = it),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }
        AnimatedCircle(
            modifier = Modifier.align(Alignment.BottomStart),
            itemWidthDp = itemWidthDp,
            spaceWidthDp = spaceWidthDp,
            currentAppScreenState = appScreenState
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomIconButtonItem(
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    // size.width实际上返回的是占屏幕多少像素，而不是dp。
                    itemWidthDp =
                        coordinates.size.width.toFloat() / screenWidthPx.toFloat() * screenWidthDp.toFloat()
                    spaceWidthDp = (screenWidthDp.toFloat() - itemWidthDp * 5) / 6
                },
                iconRes = R.drawable.mybottom_app_bar_home,
                shadowIconRes = R.drawable.mybottom_app_bar_home_shadow,
                text = "主页",
                currentAppScreenState = appScreenState,
                thisAppScreenState = AppScreenState.Main,
                onBottomBarItemClicked = onBottomBarItemClicked
            )
            BottomIconButtonItem(
                iconRes = R.drawable.mybottom_app_bar_box,
                shadowIconRes = R.drawable.mybottom_app_bar_box_shadow,
                text = "药箱",
                currentAppScreenState = appScreenState,
                thisAppScreenState = AppScreenState.Box,
                onBottomBarItemClicked = onBottomBarItemClicked
            )
            BottomIconButtonItem(
                iconRes = R.drawable.mybottom_app_bar_reminder,
                shadowIconRes = R.drawable.mybottom_app_bar_reminder_shadow,
                text = "提醒",
                currentAppScreenState = appScreenState,
                thisAppScreenState = AppScreenState.Remind,
                onBottomBarItemClicked = onBottomBarItemClicked
            )
            BottomIconButtonItem(
                iconRes = R.drawable.mybottom_app_bar_record,
                shadowIconRes = R.drawable.mybottom_app_bar_record_shadow,
                text = "记录",
                currentAppScreenState = appScreenState,
                thisAppScreenState = AppScreenState.Record,
                onBottomBarItemClicked = onBottomBarItemClicked
            )
            BottomIconButtonItem(
                iconRes = R.drawable.mybottom_app_bar_profile,
                shadowIconRes = R.drawable.mybottom_app_bar_profile_shadow,
                text = "我的",
                currentAppScreenState = appScreenState,
                thisAppScreenState = AppScreenState.Profile,
                onBottomBarItemClicked = onBottomBarItemClicked
            )
        }
    }
}

@Composable
private fun BottomIconButtonItem(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    @DrawableRes shadowIconRes: Int,
    text: String,
    currentAppScreenState: AppScreenState,
    thisAppScreenState: AppScreenState,
    onBottomBarItemClicked: (Int) -> Unit
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    // 图标被选中后的缩放比例
    val iconSelectScale by animateFloatAsState(
        targetValue =
        if (currentAppScreenState == thisAppScreenState)
            1.2f
        else
            1.0f,
        animationSpec = tween(animationTime),
        label = "图标缩放动画"
    )
    // 图标被选中后的Y轴偏移
    val iconSelectOffsetY by animateIntOffsetAsState(
        targetValue =
        if (currentAppScreenState == thisAppScreenState)
            IntOffset(0, -screenHeightDp / 40 - 8)
        else IntOffset.Zero,
        animationSpec = tween(animationTime),
        label = "图标上浮动画"
    )
    val id = when (thisAppScreenState) {
        AppScreenState.Main -> 0
        AppScreenState.Box -> 1
        AppScreenState.Remind -> 2
        AppScreenState.Record -> 3
        AppScreenState.Profile -> 4
    }
    MyIconButton(
        modifier = modifier
            .fillMaxHeight()
            .offset(y = 5.dp),
        onClick = { onBottomBarItemClicked(id) },
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.weight(0.62f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .scale(iconSelectScale)
                        .offset { iconSelectOffsetY },
                    painter = painterResource(id = iconRes),
                    contentDescription = text,
                    alignment = Alignment.BottomCenter
                )
                Column {
                    AnimatedVisibility(
                        enter = scaleIn(animationSpec = tween(animationTime)),
                        exit = scaleOut(animationSpec = tween(animationTime)),
                        visible = currentAppScreenState == thisAppScreenState
                    ) {
                        Image(
                            modifier = Modifier
                                .scale(1.2f)
                                .offset { IntOffset(10, -screenHeightDp / 40) }
                                .blur(5.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded),
                            painter = painterResource(id = shadowIconRes),
                            contentDescription = null,
                            alpha = 0.6f
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.weight(0.4f),
                text = if (currentAppScreenState != thisAppScreenState) text else ""
            )
        }
    }
}

@Composable
fun AnimatedCircle(
    modifier: Modifier = Modifier,
    itemWidthDp: Float,
    spaceWidthDp: Float,
    currentAppScreenState: AppScreenState
) {
    val screenWidthPx = Resources.getSystem().displayMetrics.widthPixels
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    // px和dp的转化比例
    val dp2Px = screenWidthPx.toFloat() / screenWidthDp.toFloat()
    // 圆的直径
    val diameterDp = itemWidthDp + 18
    // 初始X轴偏移dp
    val initOffsetXDp = (itemWidthDp / 2 + spaceWidthDp) - diameterDp / 2
    // 初始Y轴偏移dp
    val initOffsetYDp = 26
    // 执行一次动画移动的Dp
    val animateOffsetDp = itemWidthDp + spaceWidthDp
    val animatedIntOffset by animateIntOffsetAsState(
        targetValue = when (currentAppScreenState) {
            // BYD这个Offset是px，不是dp!!!
            AppScreenState.Main -> IntOffset(
                (initOffsetXDp * dp2Px).toInt(),
                -(initOffsetYDp * dp2Px).toInt()
            )

            AppScreenState.Box -> IntOffset(
                ((initOffsetXDp + animateOffsetDp) * dp2Px).toInt(),
                -(initOffsetYDp * dp2Px).toInt()
            )

            AppScreenState.Remind -> IntOffset(
                ((initOffsetXDp + animateOffsetDp * 2) * dp2Px).toInt(),
                -(initOffsetYDp * dp2Px).toInt()
            )

            AppScreenState.Record -> IntOffset(
                ((initOffsetXDp + animateOffsetDp * 3) * dp2Px).toInt(),
                -(initOffsetYDp * dp2Px).toInt()
            )

            AppScreenState.Profile -> IntOffset(
                ((initOffsetXDp + animateOffsetDp * 4) * dp2Px).toInt(),
                -(initOffsetYDp * dp2Px).toInt()
            )
        },
        animationSpec = tween(animationTime),
        label = "圆球移动动画"
    )
    Box(
        modifier = modifier
            .requiredSize(diameterDp.dp)
            .offset { animatedIntOffset }
            .clip(CircleShape)
            .background(brush = bottomAnimatedCircleGradient)
    ) {

    }
}

@Preview
@Composable
fun AppScreenPreview() {
    QixiaTheme {
        AppScreen()
    }
}