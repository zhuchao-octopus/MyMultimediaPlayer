package com.octopus.android.multimedia;

import android.content.Intent;
import android.os.Build;

import com.zhuchao.android.session.MApplication;

public class MultimediaApplication extends MApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ///MMLog.d("MultimediaApplication", "MultimediaApplication onCreate!");
        /*if (MultimediaService.mThis == null) {
            Intent intent1 = new Intent(appContext, MultimediaService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent1);
            } else {
                startService(intent1);
            }
        }*/
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
