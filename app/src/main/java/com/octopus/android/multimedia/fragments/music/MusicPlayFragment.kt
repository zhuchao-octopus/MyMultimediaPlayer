package com.octopus.android.multimedia.fragments.music

import android.os.Bundle
import android.view.View
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentMusicPlayBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.viewBinding


class MusicPlayFragment : BaseFragment(R.layout.fragment_music_play) {

    private val binding: FragmentMusicPlayBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivPlayList.setOnClickListenerWithInterval { }

    }
}