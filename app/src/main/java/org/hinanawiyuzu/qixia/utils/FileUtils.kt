package org.hinanawiyuzu.qixia.utils

import android.content.*
import android.net.*
import java.io.*

/**
 * 这个函数将原始图片复制到应用程序的内部存储目录中。
 * @param context 上下文
 * @param originalUri 原始图片的 Uri
 * @param newFileName 新图片的文件名
 * @return 新图片的 Uri
 */
fun copyImageToAppDir(context: Context, originalUri: Uri, newFileName: String): Uri? {
    var inputStream: InputStream? = null
    var outputStream: FileOutputStream? = null
    try {
        // 打开原始图片的输入流
        inputStream = context.contentResolver.openInputStream(originalUri)
        // 打开新图片的输出流
        outputStream = context.openFileOutput(newFileName, Context.MODE_PRIVATE)
        val buffer = ByteArray(1024)
        var length: Int
        // 将数据从输入流复制到输出流
        while (inputStream?.read(buffer).also { length = it ?: -1 } != -1) {
            outputStream?.write(buffer, 0, length)
        }
        // 返回新图片的 Uri
        return Uri.fromFile(File(context.filesDir, newFileName))
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        // 关闭输入流和输出流
        inputStream?.close()
        outputStream?.close()
    }
    return null
}