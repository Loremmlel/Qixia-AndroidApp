package org.hinanawiyuzu.qixia.utils

import android.content.Context
import android.widget.Toast

fun showShortToast(context: Context, message: String) {
  Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun showLongToast(context: Context, message: String) {
  Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}