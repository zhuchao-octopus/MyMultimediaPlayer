package com.octopus.android.myplayer.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.jessyan.autosize.AutoSizeConfig


internal fun Fragment.toastShort(str: String) {
    Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
}

internal fun Fragment.toastLong(str: String) {
    requireContext().toastLong(str)
}

internal fun Context.toastLong(str: String) {
    Toast.makeText(this, str, Toast.LENGTH_LONG).show()
}



/***
 * 防止重复/快速点击
 */
private var lastClickTime = 0L
private const val INTERVAL = 300
fun <T : View> T.setOnClickListenerWithInterval(block: (T) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > INTERVAL) {
            lastClickTime = currentTime
            block(this)
        }
    }
}

fun canClick(): Boolean {
    val currentTime = System.currentTimeMillis()
    if (currentTime - lastClickTime > INTERVAL) {
        lastClickTime = currentTime
        return true
    }
    return false
}


fun Context.autoCalcBaseOnWidth(): Boolean {

    // val dm = resources.displayMetrics;
    val width = AutoSizeConfig.getInstance().screenWidth
    val height = AutoSizeConfig.getInstance().screenHeight

    if (width / height < 1440f / 860f)
        return true
    return false
}

fun Context.autoCalcSizeInDp(): Float {
    if (autoCalcBaseOnWidth()) {
        return 1440f
    }
    return 860f
}



