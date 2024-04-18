package com.octopus.android.myplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.zhuchao.android.fbase.EventCourier;
import com.zhuchao.android.fbase.MMLog;

import java.util.Arrays;
import java.util.Objects;

public class GlobalBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GlobalBroadcastReceiver";

    public static void unMute(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //if (CABINET.properties.getInt("protocol") == 1570)
        {
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            MMLog.i(TAG, "GlobalBroadcastReceiver maxVolume = " + maxVolume+" currVolume = "+currVolume);
            //audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI); //取消系统静音
            if (currVolume < maxVolume) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_PLAY_SOUND);
                MMLog.i(TAG, "GlobalBroadcastReceiver currVolume = " + currVolume);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MMLog.i(TAG, "GlobalBroadcastReceiver action=" + intent.getAction());

        switch (Objects.requireNonNull(intent.getAction())) {
            case Intent.ACTION_MEDIA_MOUNTED:
                //if (CABINET.tCourierEventBus != null)
                //    CABINET.tCourierEventBus.post(new EventCourier(EventCode.USB_IN.ordinal()));
                break;
            case Intent.ACTION_MEDIA_UNMOUNTED:
            case Intent.ACTION_MEDIA_EJECT:
            case "android.intent.action.system.WHETHER_SHOW_NAVIGATION":
                break;
            case UsbManager.ACTION_USB_DEVICE_ATTACHED:
            case UsbManager.ACTION_USB_DEVICE_DETACHED:
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                MMLog.i(TAG, "getProductId = " + usbDevice.getProductId());
                MMLog.i(TAG, "getVendorId = " + usbDevice.getVendorId());
                MMLog.i(TAG, "getSerialNumber = " + usbDevice.getSerialNumber());
                break;
        }
    }

    public void registerBaseBroadcastReceiver(Context context) {
        IntentFilter usbDeviceStateFilter = new IntentFilter();
        usbDeviceStateFilter.addAction(Intent.ACTION_MEDIA_SHARED);      //如果SDCard未安装,并通过USB大容量存储共享返回
        usbDeviceStateFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);   //表明sd对象是存在并具有读/写权限
        usbDeviceStateFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);   //SDCard已卸掉,如果SDCard是存在但没有被安装
        usbDeviceStateFilter.addAction(Intent.ACTION_MEDIA_CHECKING);    //表明对象正在磁盘检查
        usbDeviceStateFilter.addAction(Intent.ACTION_MEDIA_EJECT);       //物理的拔出 SDCARD
        usbDeviceStateFilter.addAction(Intent.ACTION_MEDIA_REMOVED);     //完全拔出
        usbDeviceStateFilter.addDataScheme("file");

        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);

        usbDeviceStateFilter.addAction("android.intent.action.system.WHETHER_SHOW_NAVIGATION");
        context.registerReceiver(GlobalBroadcastReceiver.this, usbDeviceStateFilter);
    }
}
