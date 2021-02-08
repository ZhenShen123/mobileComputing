package com.hw.hwauthenticate.utils

import android.content.Context
import android.widget.Toast

object ExpandUtils {

    fun Context.toast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }

}