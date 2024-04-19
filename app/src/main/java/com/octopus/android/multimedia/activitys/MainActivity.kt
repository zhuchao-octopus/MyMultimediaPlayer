package com.octopus.android.multimedia.activitys;

import android.content.res.Resources
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ktx.immersionBar
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.utils.autoCalcBaseOnWidth
import com.octopus.android.multimedia.utils.autoCalcSizeInDp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.jessyan.autosize.AutoSizeCompat


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

    override fun getResources(): Resources {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runCatching {

                AutoSizeCompat.autoConvertDensity(
                    super.getResources(),
                    autoCalcSizeInDp(), autoCalcBaseOnWidth()
                )
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                runCatching {
                    AutoSizeCompat.autoConvertDensity(
                        super.getResources(),
                        autoCalcSizeInDp(), autoCalcBaseOnWidth()
                    )
                }
            }
        }
        return super.getResources();
    }
}