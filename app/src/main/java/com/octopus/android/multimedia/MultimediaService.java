package com.octopus.android.multimedia;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.zhuchao.android.fbase.EC;
import com.zhuchao.android.fbase.FileUtils;
import com.zhuchao.android.fbase.MMLog;
import com.zhuchao.android.fbase.MessageEvent;
import com.zhuchao.android.fbase.MethodThreadMode;
import com.zhuchao.android.fbase.TCourierSubscribe;
import com.zhuchao.android.fbase.eventinterface.EventCourierInterface;
import com.zhuchao.android.session.Cabinet;

public class MultimediaService extends Service {
    private static final String TAG = "MultimediaService";

    public MultimediaService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MMLog.d(TAG, "MultimediaService onCrete!");
        Cabinet.getEventBus().registerEventObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Cabinet.getEventBus().unRegisterEventObserver(this);
        MMLog.d(TAG,"MultimediaService onDestroy!");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @TCourierSubscribe(threadMode = MethodThreadMode.threadMode.BACKGROUND)
    public boolean onTCourierSubscribeEvent(EventCourierInterface courierInterface) {
        switch (courierInterface.getId()) {
            case MessageEvent.MESSAGE_EVENT_USB_MOUNTED -> {
                ///MMLog.d(TAG, "MESSAGE_EVENT_USB_MOUNTED");
                ///MMLog.d(TAG, FileUtils.getUDiscName(this).toString());
            }
            case MessageEvent.MESSAGE_EVENT_USB_UNMOUNT -> {
                ///MMLog.d(TAG, "MESSAGE_EVENT_USB_UNMOUNT");
            }
        }
        return true;
    }
}