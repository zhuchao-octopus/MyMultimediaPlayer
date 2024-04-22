package com.octopus.android.multimedia.fragments.bluetooth

import android.os.Bundle
import android.view.View
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentBluetoothMusicBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.viewBinding

class BluetoothMusicFragment : BaseFragment(R.layout.fragment_bluetooth_music) {
    private val binding: FragmentBluetoothMusicBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPrev.setOnClickListenerWithInterval { }
        binding.viewNext.setOnClickListenerWithInterval { }
        binding.viewPlay.setOnClickListenerWithInterval { }
    }
}