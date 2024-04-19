package com.octopus.android.multimedia;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.zhuchao.android.fbase.MMLog;

public class MultimediaService extends Service {
    private static final String TAG = "MultimediaService";

    public MultimediaService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MMLog.d(TAG, "MultimediaService onCrete!");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

}