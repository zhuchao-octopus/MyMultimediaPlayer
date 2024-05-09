package com.octopus.android.multimedia.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * 检测用户指定时间内是否操作了页面,用于在视频播放页面显示和隐藏部分内容
 * */
class CustomConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    var callBack: ((show: Boolean) -> Unit)? = null


    companion object {
        const val MESSAGE_SHOW = 1 //显示
        const val MESSAGE_HIDE = 2//隐藏
        const val TIME = 5000L
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        //重置定时器
        removeAllMessage()
        handler.sendEmptyMessage(MESSAGE_SHOW)
        handler.sendEmptyMessageDelayed(MESSAGE_HIDE, TIME)
        return super.onInterceptTouchEvent(ev)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //重置定时器
        removeAllMessage()
        handler.sendEmptyMessage(MESSAGE_SHOW)
        handler.sendEmptyMessageDelayed(MESSAGE_HIDE, TIME)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //结束定时器
        removeAllMessage()
    }

    private fun removeAllMessage() {
        handler.removeMessages(MESSAGE_SHOW)
        handler.removeMessages(MESSAGE_HIDE)
    }


    private val handler = Handler(
        Looper.getMainLooper()
    ) {
        when (it.what) {
            MESSAGE_SHOW -> {
                callBack?.invoke(true)
            }

            MESSAGE_HIDE -> {
                callBack?.invoke(false)
            }
        }
        false
    }


}