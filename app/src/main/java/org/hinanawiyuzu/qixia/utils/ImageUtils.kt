package org.hinanawiyuzu.qixia.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * 这个函数创建一个 ContentValues 对象，表示图像文件的元数据。
 * 元数据包括显示名称、MIME 类型和图像文件的相对路径。
 * @return 一个带有图像文件元数据的 ContentValues 对象。
 */
fun createUri(): ContentValues {
  return ContentValues().apply {
    // 设置图像文件的显示名称。
    put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_${System.currentTimeMillis()}")
    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
    // 如果 Android 版本为 Q（API 级别 29）或更高，则将图像文件的相对路径设置为标准图片目录。
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }
  }
}

fun Bitmap.makeWhiteTransparent(): Bitmap {
  val width = this.width
  val height = this.height
  val transparentBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
  for (y in 0 until height) {
    for (x in 0 until width) {
      val pixel = this.getPixel(x, y)
      if (pixel == Color.WHITE) {
        transparentBitmap.setPixel(x, y, Color.TRANSPARENT)
      } else {
        transparentBitmap.setPixel(x, y, pixel)
      }
    }
  }
  return transparentBitmap
}

/**
 * 将资源 ID 转换为 Bitmap 对象。
 */
fun @receiver:DrawableRes Int.toBitmap(context: Context): Bitmap {
  val drawable: Drawable? = ContextCompat.getDrawable(context, this)
  return if (drawable is BitmapDrawable) {
    drawable.bitmap
  } else {
    val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    bitmap
  }
}

fun Uri.toBitmap(context: Context): Bitmap? {
  return context.contentResolver.openInputStream(this)?.use {
    BitmapFactory.decodeStream(it)
  }
}

