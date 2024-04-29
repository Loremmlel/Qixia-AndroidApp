package org.hinanawiyuzu.qixia.utils

import android.content.*
import androidx.activity.*

/**
 * 获取上下文的Activity
 */
fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}