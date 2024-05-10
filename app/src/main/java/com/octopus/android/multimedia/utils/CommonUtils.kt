package com.octopus.android.multimedia.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.octopus.android.multimedia.statusview.DeviceNotConnectState
import com.octopus.android.multimedia.statusview.EmptyState
import com.octopus.android.multimedia.statusview.ErrorState
import com.octopus.android.multimedia.statusview.LoadingState
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.state.SuccessState
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
    val width = AutoSizeConfig.getInstance().initScreenWidthDp
    val height = AutoSizeConfig.getInstance().initScreenHeightDp

    if (width / height < 1280f / 720f)
        return false
    return true
}

fun Context.autoCalcSizeInDp(): Float {
    if (autoCalcBaseOnWidth()) {
        return 1280f
    }
    return 720f
}

fun MultiStateContainer.showSuccess() {
    show<SuccessState>()
}

fun MultiStateContainer.showError(callBack: (() -> Unit)? = null) {
    show<ErrorState> { it.callback = callBack }
}

fun MultiStateContainer.showEmpty() {
    show<EmptyState>()
}

fun MultiStateContainer.showLoading() {
    show<LoadingState>()
}

fun MultiStateContainer.showDeviceNotConnect() {
    show<DeviceNotConnectState>()
}

fun Long.convertMillisToTime(): String {
    val seconds = this / 1000
    val hours = seconds / 3600
    val minutes = seconds % 3600 / 60
    val remainingSeconds = seconds % 60
    if (hours == 0L) {
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }
    return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
}


