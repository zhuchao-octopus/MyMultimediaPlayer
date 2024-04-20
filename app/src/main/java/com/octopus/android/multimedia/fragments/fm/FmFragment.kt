package com.octopus.android.multimedia.fragments.fm

import android.os.Bundle
import android.view.View
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentFmBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.viewBinding

class FmFragment : BaseFragment(R.layout.fragment_fm) {

    private val binding: FragmentFmBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewFmOrAm.setOnClickListenerWithInterval {
            //TODO 点击fm/am
        }
        binding.viewLeft.setOnClickListenerWithInterval {
            //TODO 点击底部向左
        }
        binding.viewSearch.setOnClickListenerWithInterval {
            //TODO 点击搜索
        }
        binding.viewRight.setOnClickListenerWithInterval {
            //TODO 点击底部向右
        }
        binding.viewSignal.setOnClickListenerWithInterval {
            //TODO 点击信号塔
        }
        binding.viewDoubleCircle.setOnClickListenerWithInterval {
            //TODO 点击双圆环图标
        }
        binding.viewEq.setOnClickListenerWithInterval {
            //TODO 点击EQ
        }
        binding.viewSetting.setOnClickListenerWithInterval {
            //TODO 点击设置
        }
        binding.viewLeftChannel1.setOnClickListenerWithInterval {
            //TODO 点击左侧第一个频道
        }
        binding.viewLeftChannel2.setOnClickListenerWithInterval {
            //TODO 点击左侧第二个频道
        }
        binding.viewLeftChannel3.setOnClickListenerWithInterval {
            //TODO 点击左侧第三个频道
        }
        binding.viewRightChannel1.setOnClickListenerWithInterval {
            //TODO 点击右侧第一个频道
        }
        binding.viewRightChannel2.setOnClickListenerWithInterval {
            //TODO 点击右侧第二个频道
        }
        binding.viewRightChannel3.setOnClickListenerWithInterval {
            //TODO 点击右侧第三个频道
        }
        binding.viewAF.setOnClickListenerWithInterval {
            //TODO 点击AF
        }
        binding.viewPTY.setOnClickListenerWithInterval {
            //TODO 点击TYP
        }
        binding.viewAF.setOnClickListenerWithInterval {
            //TODO 点击TA
        }
        binding.viewRDS.setOnClickListenerWithInterval {
            //TODO 点击RDS
        }
    }
}