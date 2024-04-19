package com.octopus.android.myplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ktx.immersionBar
import com.octopus.android.myplayer.utils.toastLong
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        immersionBar {
            statusBarColor(R.color.black)
            navigationBarColor(R.color.black)
            fitsSystemWindows(true)
        }

        setContentView(R.layout.activity_main)

//        val value1 = 1280f * 1280f + 720f * 720f
//        val value2 = sqrt(value1.toDouble())
//        val value3 = value2 / 160
//        toastLong("设计屏幕尺寸为:$value3")



    }
}