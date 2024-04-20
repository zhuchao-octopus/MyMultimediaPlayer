package com.octopus.android.multimedia.fragments.fm

import android.os.Bundle
import android.view.View
import androidx.annotation.IntRange
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentFmBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.viewBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 收音机页面
 * */
class FmFragment : BaseFragment(R.layout.fragment_fm) {

    private val binding: FragmentFmBinding by viewBinding()

    private val viewModel: FmViewModel by fragmentViewModel()

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
            viewModel.searchChannel()
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

    override fun invalidate() = withState(viewModel) {
        binding.seekBar.progress = it.channelProgress //更新进度条位置
        binding.seekBar.isEnabled = !it.searching //搜索过程中,禁止拖动进度条
    }
}

data class FmState(
    @IntRange(from = 0, to = 100) val channelProgress: Int = 0, //频道进度
    val searching: Boolean = false//搜索中
) : MavericksState

class FmViewModel(initialState: FmState) : MavericksViewModel<FmState>(initialState) {

    private var searchJob: Job? = null
    fun searchChannel() {
        //模拟搜索过程
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            setState { copy(searching = true) }
            for (progress in 0..100) {
                delay(30)
                setState { copy(channelProgress = progress) }
            }
            setState { copy(searching = false) }
        }
    }
}