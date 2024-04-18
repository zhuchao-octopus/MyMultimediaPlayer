package com.octopus.android.multimedia;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.zhuchao.android.fbase.MMLog;
import com.zhuchao.android.session.MApplication;

public class MultimediaApplication extends MApplication {
    protected static Context appContext = null;//需要使用的上下文对象
    public static WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();

    private MultimediaBroadcastReceiver mMultimediaBroadcastReceiver=null;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this.getApplicationContext();
        ///layoutParams = View;
        //////////////////////////////////////////////////////////////////////////////////
        ///初始化各模块组件
        ///CABINET.InitialModules(getApplicationContext());
        //////////////////////////////////////////////////////////////////////////////////
        MMLog.d("MultimediaApplication", "MultimediaApplication onCreate!");
        mMultimediaBroadcastReceiver = new MultimediaBroadcastReceiver();
        Intent intent1 = new Intent(appContext, MultimediaService.class);
        startService(intent1);
        registerBaseBroadcastReceiver(appContext);
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
        unregisterReceiver(mMultimediaBroadcastReceiver);
        super.onTerminate();
    }

    public static Context getAppContext() {
        return appContext;
    }

    public void registerBaseBroadcastReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MEDIA_SHARED);    //如果SDCard未安装,并通过USB大容量存储共享返回
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);   //表明sd对象是存在并具有读/写权限
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED); //SDCard已卸掉,如果SDCard是存在但没有被安装
        intentFilter.addAction(Intent.ACTION_MEDIA_CHECKING);  //表明对象正在磁盘检查
        intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);     //物理的拔出 SDCARD
        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);   //完全拔出
        intentFilter.addDataScheme("file");

        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);

        intentFilter.addAction(MultimediaBroadcastReceiver.Action_OCTOPUS_HELLO);
        //context.registerReceiver(mMultimediaBroadcastReceiver, intentFilter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.registerReceiver(mMultimediaBroadcastReceiver, intentFilter, MultimediaBroadcastReceiver.Action_OCTOPUS_permission, null, Context.RECEIVER_EXPORTED);
        }
    }
}
