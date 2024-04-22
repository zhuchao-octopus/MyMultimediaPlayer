package com.octopus.android.multimedia;

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.Build
import com.airbnb.mvrx.Mavericks
import com.zhuchao.android.fbase.MMLog
import com.zhuchao.android.session.Cabinet
import com.zhuchao.android.session.MApplication

class MultimediaApplication : MApplication() {

    override fun onCreate() {
        super.onCreate()
        MMLog.d("MultimediaApplication", "MultimediaApplication onCreate!")
        Mavericks.initialize(this)
        //////////////////////////////////////////////////////////////////////////////////
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}
