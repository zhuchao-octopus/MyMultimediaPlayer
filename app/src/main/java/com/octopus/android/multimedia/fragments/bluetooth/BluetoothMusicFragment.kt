package com.octopus.android.multimedia.fragments.bluetooth

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.car.api.ApiBt
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentBluetoothMusicBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.viewBinding

/**
 * 蓝牙音乐页面
 * */
class BluetoothMusicFragment : BaseFragment(R.layout.fragment_bluetooth_music) {
    private val binding: FragmentBluetoothMusicBinding by viewBinding()
    private val bluetoothViewModel: BluetoothViewModel by activityViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPrev.setOnClickListenerWithInterval {
            bluetoothViewModel.prevMusic()
        }
        binding.viewNext.setOnClickListenerWithInterval {
            bluetoothViewModel.nextMusic()
        }
        binding.viewPlay.setOnClickListenerWithInterval {
            bluetoothViewModel.playPauseMusic()
        }
    }

    override fun invalidate() = withState(bluetoothViewModel) {
        binding.tvName.text = it.musicTitle
        binding.tvAuthor.text = it.musicArtist
        if (it.musicState == ApiBt.PLAYSTATE_PAUSE) {
            binding.ivPlay.setImageResource(R.mipmap.bt_music_play)
            binding.tvPlay.text = "Play"
        } else if (it.musicState == ApiBt.PLAYSTATE_PLAY) {
            binding.ivPlay.setImageResource(R.mipmap.bt_music_pause)
            binding.tvPlay.text = "Pause"
        }
    }
}