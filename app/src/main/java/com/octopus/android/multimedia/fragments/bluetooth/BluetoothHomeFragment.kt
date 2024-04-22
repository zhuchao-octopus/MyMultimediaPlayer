package com.octopus.android.multimedia.fragments.bluetooth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentBluetoothHomeBinding
import com.octopus.android.multimedia.databinding.FragmentVideoSortBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.fragments.FolderFragment
import com.octopus.android.multimedia.fragments.PlayListFragment
import com.octopus.android.multimedia.fragments.SDFragment
import com.octopus.android.multimedia.fragments.USBFragment
import com.octopus.android.multimedia.fragments.VideoSortViewModel
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.viewBinding

/**
 * 蓝牙主页
 * */
class BluetoothHomeFragment : BaseFragment(R.layout.fragment_bluetooth_home) {

    private val binding: FragmentBluetoothHomeBinding by viewBinding()
    private val viewModel: VideoSortViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewDial.setOnClickListenerWithInterval {
            it.isSelected = true
            viewModel.setIndex(0)
        }
        binding.viewPhone.setOnClickListenerWithInterval {
            it.isSelected = true
            viewModel.setIndex(1)
        }
        binding.viewCallLog.setOnClickListenerWithInterval {
            it.isSelected = true
            viewModel.setIndex(2)
        }
        binding.viewSetting.setOnClickListenerWithInterval {
            it.isSelected = true
            viewModel.setIndex(3)
        }
        binding.viewPair.setOnClickListenerWithInterval {
            it.isSelected = true
            viewModel.setIndex(4)
        }
        binding.viewMusic.setOnClickListenerWithInterval {
            it.isSelected = true
            viewModel.setIndex(5)
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setIndex(position)
            }
        })

        //配置viewpager
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 6
            }

            override fun createFragment(position: Int): Fragment {
                if (position == 0) {
                    return BluetoothDialFragment()
                } else if (position == 1) {
                    return BluetoothPhoneFragment()
                } else if (position == 2) {
                    return BluetoothCallLogFragment()
                } else if (position == 3) {
                    return BluetoothSettingFragment()
                } else if (position == 4) {
                    return BluetoothPairFragment()
                } else if (position == 5) {
                    return BluetoothMusicFragment()
                }
                return Fragment()
            }
        }


        binding.viewDial.isSelected =true

    }

    override fun invalidate() = withState(viewModel) {
        if (it.index != binding.viewPager.currentItem) {
            binding.viewPager.setCurrentItem(it.index,false)
        }

        binding.viewDial.isSelected = it.index == 0
        binding.viewPhone.isSelected = it.index == 1
        binding.viewCallLog.isSelected = it.index == 2
        binding.viewSetting.isSelected = it.index == 3
        binding.viewPair.isSelected = it.index == 4
        binding.viewMusic.isSelected = it.index == 5
    }
}