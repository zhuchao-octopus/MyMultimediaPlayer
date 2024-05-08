package com.octopus.android.multimedia.fragments.music

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentMusicPlayBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.viewBinding


class MusicPlayFragment : BaseFragment(R.layout.fragment_music_play) {

    private val binding: FragmentMusicPlayBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivPlayList.setOnClickListenerWithInterval {
            //返回播放列表
            findNavController().popBackStack()
        }

        binding.viewPlay.setOnClickListenerWithInterval {
            //TODO 播放暂停
        }

        binding.viewPrev.setOnClickListenerWithInterval {
            //TODO 上一首
        }

        binding.viewNext.setOnClickListenerWithInterval {
            //TODO 下一首
        }

        binding.viewMode.setOnClickListenerWithInterval {
            //TODO 切花播放模式,如单曲循环,随机等
        }

        binding.viewCollection.setOnClickListenerWithInterval {
            //TODO  收藏,取消收藏
        }

    }

    override fun invalidate() {
        super.invalidate()

        //TODO 音乐名称
        binding.tvMusicName.text = "爱情废柴"
        //TODO 演唱者名称
        binding.tvArtistsName.text = "周杰伦"
        //TODO 专辑名称
        binding.tvMusicName.text = "叶惠美"
        //TODO 播放时间
        binding.tvStartTime.text = "00:00"
        //TODO 总时间
        binding.tvStartTime.text = "03:00"
        //TODO 播放进度
        binding.seekBar.progress = 30
        //TODO 收藏状态
        binding.ivCollection.isSelected = false
        //TODO 播放模式
        binding.ivMode.isSelected = true
    }
}