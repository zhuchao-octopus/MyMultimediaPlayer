package com.octopus.android.multimedia.fragments.fm

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.core.view.postDelayed
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.car.api.ApiKit
import com.car.api.ApiRadio
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentFmBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.toastLong
import com.octopus.android.multimedia.utils.viewBinding
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
            //TODO 点击fm/am 切换调频模式,切换页面中khz和mkz单位,调整步进,当进入am模式后,隐藏底部af,pty,ta,rds按钮
            viewModel.toggleFmOrAm()
        }
        binding.viewLeft.setOnClickListenerWithInterval {
            //TODO 点击底部向左 从当前位置开始,向左循环搜索第一个有信号的频道,如果有,则播放当前频道
            viewModel.prevSearch()
        }
        binding.viewSearch.setOnClickListenerWithInterval {
            //TODO 点击搜索 从头开始向后搜索,将搜索到的频道,加入到频道列表中,搜索完毕后,自动播放每个频道5秒,然后播放下一个频道,当用户点击某个频道后,则取消自动播放逻辑
            viewModel.search()
        }
        binding.viewRight.setOnClickListenerWithInterval {
            //TODO 点击底部向右 从当前位置开始,向右循环搜索第一个有信号的频道,如果有,则播放当前频道
            viewModel.nextSearch()
        }
        binding.viewSignal.setOnClickListenerWithInterval {
            //TODO 点击信号塔

        }
        binding.viewDoubleCircle.setOnClickListenerWithInterval {
            //TODO 点击双圆环图标 显示或隐藏 st 和双圆环 图标 ,当有信号时,显示双圆环,当没有信号时,不显示双圆环
        }
        binding.viewEq.setOnClickListenerWithInterval {
            //TODO 点击EQ
        }
        binding.viewSetting.setOnClickListenerWithInterval {
            //TODO 点击设置
            viewModel.test()
        }
        binding.viewLeftChannel1.setOnClickListenerWithInterval {
            //TODO 点击左侧第一个频道
            viewModel.selectSearchChannel(0)
        }
        binding.viewLeftChannel2.setOnClickListenerWithInterval {
            //TODO 点击左侧第二个频道
            viewModel.selectSearchChannel(1)
        }
        binding.viewLeftChannel3.setOnClickListenerWithInterval {
            //TODO 点击左侧第三个频道
            viewModel.selectSearchChannel(2)
        }
        binding.viewRightChannel1.setOnClickListenerWithInterval {
            //TODO 点击右侧第一个频道
            viewModel.selectSearchChannel(3)
        }
        binding.viewRightChannel2.setOnClickListenerWithInterval {
            //TODO 点击右侧第二个频道
            viewModel.selectSearchChannel(4)
        }
        binding.viewRightChannel3.setOnClickListenerWithInterval {
            //TODO 点击右侧第三个频道
            viewModel.selectSearchChannel(5)
        }
        binding.viewAF.setOnClickListenerWithInterval {
            //TODO 点击AF 点击AF按钮后,在页面中会出现闪烁的AF字样,再次点击停止闪烁,当切换为AM模式后,隐藏显示
        }
        binding.viewPTY.setOnClickListenerWithInterval {
            //TODO 点击TYP 显示pty类型选择弹窗,弹窗中有pty类型列表,默认选中NO_PTY,点击后选中,选中后可点击ok或者关闭页面

            //显示列表对话框
            AlertDialog.Builder(requireContext()).apply {
                setItems(ApiRadio.PTY_DISPLAY) { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                    toastLong("选择了${ApiRadio.PTY_DISPLAY[i]}")
                }

                setNegativeButton("取消") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
                create()
                show()
            }

        }
        binding.viewAF.setOnClickListenerWithInterval {
            //TODO 点击TA  点击后开始向后循环扫描,中间频率显示TA SEEK
        }
        binding.viewRDS.setOnClickListenerWithInterval {
            //TODO 点击RDS 弹出rds设置,如region ,pi,ta,retune,这里趣智达并没有提供相应的接口
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    viewModel.setChannelByProgress2(seekBar.progress)
                }

            }
        })
    }

    override fun invalidate() = withState(viewModel) {

        binding.seekBar.isEnabled = !it.searching //搜索过程中,禁止拖动进度条

        it.getCurrentBlandMode().apply {
            val visible = if (supportRDS) View.VISIBLE else View.INVISIBLE
            binding.viewPTY.visibility = visible
            binding.viewAF.visibility = visible
            binding.viewTA.visibility = visible
            binding.viewRDS.visibility = visible

            binding.tvChannel.text = Band.display(type, channel)
            binding.tvUnit.text = unit

            binding.tvSearchChannel1.text =
                if (searchChannels.isNotEmpty()) Band.display(
                    type,
                    searchChannels[0]
                ) else Band.display(type, minChannel)
            binding.tvSearchChannel2.text =
                if (searchChannels.size > 1) Band.display(
                    type,
                    searchChannels[1]
                ) else Band.display(type, minChannel)
            binding.tvSearchChannel3.text =
                if (searchChannels.size > 2) Band.display(
                    type,
                    searchChannels[2]
                ) else Band.display(type, minChannel)
            binding.tvSearchChannel4.text =
                if (searchChannels.size > 3) Band.display(
                    type,
                    searchChannels[3]
                ) else Band.display(type, minChannel)
            binding.tvSearchChannel5.text =
                if (searchChannels.size > 4) Band.display(
                    type,
                    searchChannels[4]
                ) else Band.display(type, minChannel)
            binding.tvSearchChannel6.text =
                if (searchChannels.size > 5) Band.display(
                    type,
                    searchChannels[5]
                ) else Band.display(type, minChannel)

            binding.tvSearchUnit1.text = unit
            binding.tvSearchUnit2.text = unit
            binding.tvSearchUnit3.text = unit
            binding.tvSearchUnit4.text = unit
            binding.tvSearchUnit5.text = unit
            binding.tvSearchUnit6.text = unit

            binding.viewLeftChannel1.isSelected = selectedSearchChannelIndex == 0
            binding.viewLeftChannel2.isSelected = selectedSearchChannelIndex == 1
            binding.viewLeftChannel3.isSelected = selectedSearchChannelIndex == 2
            binding.viewRightChannel1.isSelected = selectedSearchChannelIndex == 3
            binding.viewRightChannel2.isSelected = selectedSearchChannelIndex == 4
            binding.viewRightChannel3.isSelected = selectedSearchChannelIndex == 5




            binding.tvMinChannel.text = Band.display(type, minChannel)
            binding.tvMaxChannel.text = Band.display(type, maxChannel)




            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.seekBar.min = minChannel
            }
            binding.seekBar.max = maxChannel

            binding.seekBar.postDelayed(50){
                lifecycleScope.launch {
                    binding.seekBar.progress = channel
                }
            }

        }

        Unit

    }
}










