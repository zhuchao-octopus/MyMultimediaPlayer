package com.octopus.android.multimedia;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.view.WindowManager;

import com.zhuchao.android.fbase.MMLog;
import com.zhuchao.android.session.MApplication;

public class MultimediaApplication extends MApplication {
    protected static Context appContext = null;//需要使用的上下文对象
    public static WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
    public static Context getAppContext()
    {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this.getApplicationContext();
        ///layoutParams = View;
        //////////////////////////////////////////////////////////////////////////////////
        ///初始化各模块组件
        ///CABINET.InitialModules(getApplicationContext());
        //////////////////////////////////////////////////////////////////////////////////
        MMLog.d("MultimediaApplication","MultimediaApplication is crated!");
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
