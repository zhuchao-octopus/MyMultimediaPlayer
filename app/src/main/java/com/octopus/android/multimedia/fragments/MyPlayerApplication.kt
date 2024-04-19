package com.octopus.android.multimedia.fragments;

import com.airbnb.mvrx.Mavericks
import com.zhuchao.android.session.MApplication

class MyPlayerApplication : MApplication() {
    override fun onCreate() {
        super.onCreate()
        Mavericks.initialize(this)
    }
}
