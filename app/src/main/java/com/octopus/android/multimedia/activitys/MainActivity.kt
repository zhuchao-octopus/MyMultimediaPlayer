package com.octopus.android.multimedia.activitys;

import android.content.res.Resources
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.utils.autoCalcBaseOnWidth
import com.octopus.android.multimedia.utils.autoCalcSizeInDp
import com.zhuchao.android.session.TPlayManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.jessyan.autosize.AutoSizeCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //immersionBar {
        //    statusBarColor(R.color.black)
        //    navigationBarColor(R.color.black)
        //   fitsSystemWindows(true)
        //}

        setContentView(R.layout.activity_main)
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

    override fun onStop() {
        super.onStop()
        lifecycleScope.launch { TPlayManager.getInstance().saveToFile() }
    }
}