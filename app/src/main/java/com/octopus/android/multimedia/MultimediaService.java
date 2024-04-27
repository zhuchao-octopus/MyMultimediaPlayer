package com.octopus.android.multimedia;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.zhuchao.android.fbase.MMLog;

public class MultimediaService extends Service {
    private static final String TAG = "MultimediaService";
    public static MultimediaService mThis = null;
    public MultimediaService() {
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();
        mThis = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1,new Notification());
        }
        //MMLog.d(TAG, "MultimediaService onCrete!");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

}