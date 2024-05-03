package org.hinanawiyuzu.qixia.components

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavBackStackEntry
import coil.compose.rememberAsyncImagePainter

/**
 * 全屏图片查看
 * @param modifier Modifier
 * @param uri Uri 图片地址
 * @param onClick Function0<Unit>  单击图片
 */
@Composable
fun FullScreenImage(
  modifier: Modifier = Modifier,
  uri: Uri,
  onClick: () -> Unit = {}
) {

  var scale by remember { mutableFloatStateOf(1f) }
  var offset by remember { mutableStateOf(Offset.Zero) }
  val state = rememberTransformableState { zoomChange, _, _ ->
    scale = (zoomChange * scale).coerceAtLeast(1f)
  }
  Surface(
    color = Color.DarkGray,
    modifier = modifier
      .fillMaxSize()
      .pointerInput(Unit) {
        detectTapGestures(
          onDoubleTap = {
            scale = 1f
            offset = Offset.Zero
          },
          onTap = {
            onClick.invoke()
          }
        )
      }
  ) {
    Image(
      painter = rememberAsyncImagePainter(uri),
      contentDescription = "",
      modifier = Modifier
        .fillMaxSize()
        .transformable(state = state)
        .graphicsLayer(
          scaleX = scale,
          scaleY = scale,
          translationX = offset.x,
          translationY = offset.y
        )
        .pointerInput(Unit) {
          detectDragGestures { _, dragAmount ->
            offset += dragAmount
          }
        }

    )
  }
}

@Stable
@Composable
fun FullScreenImageView(
  modifier: Modifier = Modifier,
  onDismiss: () -> Unit = {},
  backStackEntry: NavBackStackEntry
) {
  val uriString = backStackEntry.arguments?.getString("uri")
  val uri = uriString?.replace("*", "/")?.toUri() ?: Uri.EMPTY
  val bitmap = BitmapFactory.decodeStream(LocalContext.current.contentResolver.openInputStream(uri))
  Box(
    modifier = modifier
      .fillMaxSize()
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null
      ) { onDismiss.invoke() },
    contentAlignment = Alignment.Center
  ) {
    Image(
      bitmap = bitmap.asImageBitmap(),
      contentDescription = null,
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop
    )
  }
}