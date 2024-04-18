package com.octopus.android.multimedia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;

import com.zhuchao.android.fbase.MMLog;
import com.zhuchao.android.session.TWatchManService;

import java.util.Objects;

public class MultimediaBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MultimediaBroadcastReceiver";
    private static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

    public MultimediaBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MMLog.i(TAG, "MultimediaBroadcastReceiver action=" + intent.getAction());

        switch (Objects.requireNonNull(intent.getAction())) {
            case ACTION_BOOT_COMPLETED:
                Intent intent1 = new Intent(context, MultimediaService.class);
                context.startService(intent1);
            break;
            case Intent.ACTION_MEDIA_MOUNTED:
                ///if (CABINET.tCourierEventBus != null)
                ///    CABINET.tCourierEventBus.post(new EventCourier(EventCode.USB_IN.ordinal()));
                break;
            case Intent.ACTION_MEDIA_UNMOUNTED:
            case Intent.ACTION_MEDIA_EJECT:
                break;
            case UsbManager.ACTION_USB_DEVICE_ATTACHED:
            case UsbManager.ACTION_USB_DEVICE_DETACHED:
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                MMLog.i(TAG, "getProductId=" + usbDevice.getProductId());
                MMLog.i(TAG, "getVendorId=" + usbDevice.getVendorId());
                MMLog.i(TAG, "getSerialNumber=" + usbDevice.getSerialNumber());
                break;
        }
    }


    public static void unMute(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //if (CABINET.properties.getInt("protocol") == 1570)
        {
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            MMLog.i(TAG, "GlobalBroadcastReceiver maxVolume = " + maxVolume + " currVolume = " + currVolume);
            //audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI); //取消系统静音
            if (currVolume < maxVolume) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_PLAY_SOUND);
                MMLog.i(TAG, "GlobalBroadcastReceiver currVolume = " + currVolume);
            }
        }
    }

}
