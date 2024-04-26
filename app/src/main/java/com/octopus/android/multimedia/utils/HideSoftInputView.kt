package com.octopus.android.multimedia.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout

class HideSoftInputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        hideSoftInput()
        return super.onInterceptTouchEvent(ev)
    }

    //隐藏软键盘
    private fun hideSoftInput() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}