package com.octopus.android.multimedia.fragments;

import static android.content.Context.BIND_AUTO_CREATE;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.KeyEventDispatcher;

import com.octopus.android.multimedia.R;
import com.octopus.android.multimedia.databinding.FragmentVideoBinding;
import com.zhuchao.android.car.IMyCarAidlInterface;
import com.zhuchao.android.detect.FdActivity;
import com.zhuchao.android.fbase.MMLog;
import com.zhuchao.android.fbase.MethodThreadMode;
import com.zhuchao.android.fbase.TAppProcessUtils;
import com.zhuchao.android.fbase.TCourierSubscribe;
import com.zhuchao.android.fbase.eventinterface.EventCourierInterface;
import com.zhuchao.android.fbase.eventinterface.PlaybackEvent;
import com.zhuchao.android.fbase.eventinterface.PlayerCallback;
import com.zhuchao.android.fbase.eventinterface.PlayerStatusInfo;
import com.zhuchao.android.session.BaseFragment;

import com.zhuchao.android.session.TPlayManager;
import com.zhuchao.android.video.OMedia;
import com.zhuchao.android.video.VideoList;

import java.util.List;

public class VideoFragment extends BaseFragment implements PlayerCallback {
    private static final String TAG = "FirstFragment";
    private FragmentVideoBinding binding;
    //private final OMedia oMedia = new OMedia("/storage/USBdisk1/Gentleman-1080P.mp4");
    private final OMedia oMedia = new OMedia("/storage/USBdisk2/Gentleman-1080P.mp4");

    public static final VideoList defaultPlayingList_video = new VideoList(null);//视频
    public static final VideoList defaultPlayingList_audio = new VideoList(null);//音乐
    public static final VideoList defaultPlayingList_picture = new VideoList(null);//图片
    private TPlayManager tPlayManager = TPlayManager.getInstance(getContext());
    private IMyCarAidlInterface carBinder;
    private Context mContext = null;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentVideoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ///binding.button1.setOnClickListener(new View.OnClickListener() {
        ///    @Override
        ///    public void onClick(View view) {
        ///        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
        ///    }
        ///});
        mContext = this.getContext();
        //MMLog.d(TAG, TAG+" " + TAppProcessUtils.getCurrentProcessNameAndId(mContext));
        ///Intent intent = new Intent(getContext(), FdActivity.class);
        ///startActivity(intent);
        tPlayManager = TPlayManager.getInstance(getContext());
        defaultPlayingList_video.add(oMedia);
        tPlayManager.addPlayList("video", defaultPlayingList_video);
        tPlayManager.addPlayList("audio", defaultPlayingList_audio);
        tPlayManager.addPlayList("picture", defaultPlayingList_picture);
        tPlayManager.callback(this);
        tPlayManager.setSurfaceView((SurfaceView) binding.getRoot().findViewById(R.id.surfaceView));
        binding.getRoot().findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //oMedia.with(getContext()).build().playOn((SurfaceView) binding.getRoot().findViewById(R.id.surfaceView));
                tPlayManager.playPause();
            }
        });

        binding.getRoot().findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tPlayManager.playNext();
            }
        });

        binding.getRoot().findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tPlayManager.autoPlay();
            }
        });

        binding.getRoot().findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///Intent intent = new Intent(mContext, MMCarService.class);
                ///MMLog.d(TAG,"mContext.bindService(intent, serviceConnection, BIND_AUTO_CREATE)");
                Intent intent = new Intent("com.zhuchao.android.car.action.MMCarService");
                //intent.setPackage(mContext.getPackageName());
                intent.setComponent(new ComponentName("com.zhuchao.android.car","com.zhuchao.android.car.service.MMCarService"));
                //Intent newIntent = new Intent(createExplicitFromImplicitIntent(mContext,intent));
                mContext.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            }
        });

        binding.getRoot().findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(carBinder == null) {
                    MMLog.d(TAG, "carBinder = NULL!");
                    return;
                }
                try {
                    MMLog.d(TAG,carBinder.getHelloData());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        binding.getRoot().findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent("com.octopus.android.action.OCTOPUS_HELLO");
               intent.setPackage(mContext.getPackageName());
               intent.setComponent(new ComponentName("com.zhuchao.android.session","com.zhuchao.android.session.GlobalBroadcastReceiver"));
               mContext.sendBroadcast(intent);
            }
        });
        binding.getRoot().findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FdActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onEventPlayerStatus(PlayerStatusInfo playerStatusInfo) {
        switch (playerStatusInfo.getEventType()) {
            case PlaybackEvent.Status_NothingIdle://播放器空闲
            case PlaybackEvent.Status_Opening://正在打开媒体文件
            case PlaybackEvent.Status_Buffering://正在缓冲
            case PlaybackEvent.Status_Playing://正在播放
            case PlaybackEvent.Status_Ended://播放完成
            case PlaybackEvent.Status_Error://错误
                break;
        }
        //MMLog.d(TAG,playerStatusInfo.toString());
    }

    @TCourierSubscribe(threadMode = MethodThreadMode.threadMode.MAIN)
    public boolean onCourierEvent(EventCourierInterface courierInterface) {
        //todo 异步事件
        return true;
    }
    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MMLog.i(TAG, "onServiceConnected: succeed to connect service.");
            carBinder = IMyCarAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onBindingDied(ComponentName name) {
            ServiceConnection.super.onBindingDied(name);
        }

        @Override
        public void onNullBinding(ComponentName name) {
            ServiceConnection.super.onNullBinding(name);
        }
    };
    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        @SuppressLint("QueryPermissionsNeeded") List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }
}