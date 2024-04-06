package org.hinanawiyuzu.qixia.components

import android.graphics.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.drawscope.*

/**
 * 实现渐变色的文本
 */
@Composable
fun GradientText(
    modifier: Modifier = Modifier,
    text: String,
    size: Float,
    colors: List<Color>
) {
    val paint = Paint().asFrameworkPaint()
    val gradientShader: Shader = LinearGradientShader(
        from = Offset(0f, 0f),
        to = Offset(0f, size),
        colors
    )

    Canvas(modifier.fillMaxSize()) {
        paint.apply {
            isAntiAlias = true
            textSize = size
        }
        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.nativeCanvas.drawText(text, 0f, size, paint)
            canvas.restore()
            paint.shader = gradientShader
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            paint.maskFilter = null
            canvas.nativeCanvas.drawText(text, 0f, size, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
            canvas.nativeCanvas.drawText(text, 0f, size, paint)
        }
        paint.reset()
    }
}