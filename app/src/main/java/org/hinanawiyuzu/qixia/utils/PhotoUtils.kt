package org.hinanawiyuzu.qixia.utils

import android.content.*
import android.os.*
import android.provider.*

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

