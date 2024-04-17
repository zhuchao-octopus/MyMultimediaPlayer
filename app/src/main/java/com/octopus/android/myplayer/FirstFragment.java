package com.octopus.android.myplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.octopus.android.myplayer.databinding.FragmentFirstBinding;
import com.zhuchao.android.detect.FdActivity;
import com.zhuchao.android.session.TPlayManager;
import com.zhuchao.android.video.OMedia;
import com.zhuchao.android.video.VideoList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    //private final OMedia oMedia = new OMedia("/storage/USBdisk1/Gentleman-1080P.mp4");
    private final OMedia oMedia = new OMedia("/storage/USBdisk2/Gentleman-1080P.mp4");

    public static final VideoList defaultPlayingList_video = new VideoList(null);//视频
    public static final VideoList defaultPlayingList_audio = new VideoList(null);//音乐
    public static final VideoList defaultPlayingList_picture = new VideoList(null);//图片
    TPlayManager tPlayManager = TPlayManager.getInstance(getContext());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
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

        ///Intent intent = new Intent(getContext(), FdActivity.class);
        ///startActivity(intent);

        defaultPlayingList_video.add(oMedia);
        tPlayManager.addPlayList("video", defaultPlayingList_video);
        tPlayManager.addPlayList("audio", defaultPlayingList_audio);
        tPlayManager.addPlayList("picture", defaultPlayingList_picture);

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

}