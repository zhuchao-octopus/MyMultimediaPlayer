package com.octopus.android.multimedia.fragments.fm

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnticipateInterpolator
import android.view.animation.CycleInterpolator
import android.view.animation.ScaleAnimation
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.postDelayed
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.car.api.ApiKit
import com.car.api.ApiMain
import com.car.api.ApiRadio
import com.car.api.CarService
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentFmBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.viewBinding
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.Flow


/**
 * 收音机页面
 * */
class FmFragment : BaseFragment(R.layout.fragment_fm) {

    private val binding: FragmentFmBinding by viewBinding()

    private val viewModel: FmViewModel by activityViewModel()

    private val channelTextViews = lazy {
        arrayOf(
            binding.tvSearchChannel1,
            binding.tvSearchChannel2,
            binding.tvSearchChannel3,
            binding.tvSearchChannel4,
            binding.tvSearchChannel5,
            binding.tvSearchChannel6
        )
    }

    private val channelViews = lazy {
        arrayOf(
            binding.viewLeftChannel1,
            binding.viewLeftChannel2,
            binding.viewLeftChannel3,
            binding.viewRightChannel1,
            binding.viewRightChannel2,
            binding.viewRightChannel3
        )
    }

    private val channelUnitTextViews = lazy {
        arrayOf(
            binding.tvSearchUnit1,
            binding.tvSearchUnit2,
            binding.tvSearchUnit3,
            binding.tvSearchUnit4,
            binding.tvSearchUnit5,
            binding.tvSearchUnit6
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewFmOrAm.setOnClickListenerWithInterval {
            // 点击fm/am 切换调频模式,切换页面中khz和mkz单位,调整步进,当进入am模式后,隐藏底部af,pty,ta,rds按钮
            viewModel.toggleFmOrAm()
        }
        binding.viewLeft.setOnClickListenerWithInterval {
            // 点击底部向左 从当前位置开始,向左循环搜索第一个有信号的频道,如果有,则播放当前频道
            viewModel.prevSearch()
        }
        binding.viewSearch.setOnClickListenerWithInterval {
            // 点击搜索 从头开始向后搜索,将搜索到的频道,加入到频道列表中,搜索完毕后,自动播放每个频道5秒,然后播放下一个频道,当用户点击某个频道后,则取消自动播放逻辑
            viewModel.search()
        }
        binding.viewRight.setOnClickListenerWithInterval {
            // 点击底部向右 从当前位置开始,向右循环搜索第一个有信号的频道,如果有,则播放当前频道
            viewModel.nextSearch()
        }
        binding.viewSignal.setOnClickListenerWithInterval {
            //TODO 点击信号塔
        }
        binding.viewDoubleCircle.setOnClickListenerWithInterval {
            // 点击双圆环图标 显示或隐藏 st 和双圆环 图标 ,当有信号时,显示双圆环,当没有信号时,不显示双圆环
            viewModel.toggleStEnable()
        }
        binding.viewEq.setOnClickListenerWithInterval {
            //TODO 点击EQ
        }
        binding.viewSetting.setOnClickListenerWithInterval {
            //TODO 点击设置
        }
        binding.viewLeftChannel1.setOnClickListenerWithInterval {
            // 点击左侧第一个频道
            viewModel.selectSearchChannel(0)
        }
        binding.viewLeftChannel2.setOnClickListenerWithInterval {
            // 点击左侧第二个频道
            viewModel.selectSearchChannel(1)
        }
        binding.viewLeftChannel3.setOnClickListenerWithInterval {
            // 点击左侧第三个频道
            viewModel.selectSearchChannel(2)
        }
        binding.viewRightChannel1.setOnClickListenerWithInterval {
            // 点击右侧第一个频道
            viewModel.selectSearchChannel(3)
        }
        binding.viewRightChannel2.setOnClickListenerWithInterval {
            // 点击右侧第二个频道
            viewModel.selectSearchChannel(4)
        }
        binding.viewRightChannel3.setOnClickListenerWithInterval {
            // 点击右侧第三个频道
            viewModel.selectSearchChannel(5)
        }
        binding.viewPrevChannel.setOnClickListenerWithInterval {
            //点击上一个频道
            viewModel.prevChannel()
        }
        binding.viewPrevChannel.setOnLongClickListener {
            //长按上一个频道
            viewModel.prevSearch()
            true
        }
        binding.viewNextChannel.setOnClickListenerWithInterval {
            //点击下一个频道
            viewModel.nextChannel()
        }
        binding.viewNextChannel.setOnLongClickListener {
            //长按下一个频道
            viewModel.nextSearch()
            true
        }

        binding.viewPTY.setOnClickListenerWithInterval {
            // 点击TYP 显示pty类型选择弹窗,弹窗中有pty类型列表,默认选中NO_PTY,点击后选中,选中后可点击ok或者关闭页面

            //显示列表对话框
            AlertDialog.Builder(requireContext()).apply {
                setItems(ApiRadio.PTY_DISPLAY) { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                    //toastLong("选择了${ApiRadio.PTY_DISPLAY[i]}")
                    viewModel.setRdsSeek(i)
                }

                setNegativeButton("取消") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
                create()
                show()
            }

        }
        binding.viewAF.setOnClickListenerWithInterval {
            // 点击AF 点击AF按钮后,在页面中会出现闪烁的AF字样,再次点击停止闪烁,当切换为AM模式后,隐藏显示
            viewModel.toggleRdsAf()
        }
        binding.viewRDS.setOnClickListenerWithInterval {
            //TODO 点击RDS 弹出rds设置,如region ,pi,ta,retune,这里趣智达并没有提供相应的接口
        }
        binding.viewTA.setOnClickListenerWithInterval {
            // 点击TA  点击后开始向后循环扫描,中间频率显示TA SEEK
            viewModel.toggleRdsTa()
        }

        binding.seekBarFm.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

        binding.seekBarAm.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

        binding.seekBarAm.isEnabled = !it.searching //搜索过程中,禁止拖动进度条
        binding.seekBarFm.isEnabled = !it.searching

        it.currentBand.apply {
            val visible = if (supportRDS) View.VISIBLE else View.INVISIBLE
            binding.viewPTY.visibility = visible
            binding.viewAF.visibility = visible
            binding.viewTA.visibility = visible
            binding.viewRDS.visibility = visible

            binding.tvChannel.text = getDisplayChannel(channel)
            binding.tvUnit.text = unit

            for (index in 0..5) {
                val realIndex = searchPageIndex * 6 + index
                val channel = searchChannels.getOrElse(realIndex) { minChannel }
                val displayChannel = getDisplayChannel(channel)
                channelTextViews.value[index].text = displayChannel
                channelUnitTextViews.value[index].text = unit
                channelViews.value[index].isSelected = realIndex == getSelectedSearchIndex()

            }

            binding.tvMinChannel.text = getDisplayChannel(minChannel)
            binding.tvMaxChannel.text = getDisplayChannel(maxChannel)


            if (it.currentBand is FMBand) {
                binding.seekBarFm.visibility = View.VISIBLE
                binding.seekBarAm.visibility = View.INVISIBLE
                binding.seekBarFm.progress = channel
            } else if (it.currentBand is AMBand) {
                binding.seekBarFm.visibility = View.INVISIBLE
                binding.seekBarAm.visibility = View.VISIBLE
                binding.seekBarAm.progress = channel
            }

            if (supportST) {
                if (it.stEnable)
                    binding.tvST.visibility = View.VISIBLE
                else
                    binding.tvST.visibility = View.INVISIBLE

            } else {
                binding.tvST.visibility = View.INVISIBLE
            }


            if (supportRDS) {
                binding.tvAF.visibility =
                    if (it.afEnable && it.afVisible) View.VISIBLE else View.INVISIBLE
                binding.tvTA.visibility = if (it.taEnable) View.VISIBLE else View.INVISIBLE
                binding.tvST.visibility = if (it.stEnable) View.VISIBLE else View.INVISIBLE
                binding.tvDoubleCircle.visibility = View.INVISIBLE
            } else {
                binding.tvAF.visibility = View.INVISIBLE
                binding.tvTA.visibility = View.INVISIBLE
                binding.tvST.visibility = View.INVISIBLE
                binding.tvDoubleCircle.visibility = View.INVISIBLE
            }

            if (!binding.tvName.text.equals(getDisplayName())) {
                showChannelToggleAnimation()
            }

            binding.tvName.text = getDisplayName()
        }

        Unit
    }

    override fun onResume() {
        super.onResume()

        //打开收音机
        ApiMain.appId(ApiMain.APP_ID_RADIO, ApiMain.APP_ID_RADIO)
        CarService.me().cmd(ApiRadio.CMD_POWER, ApiKit.ON)
    }

    /**
     * 显示频道切换动画
     * */
    private fun showChannelToggleAnimation() {
        //缩放
        val scaleAnimation = ScaleAnimation(1f, 0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
        //渐变
        val alphaAnimation = AlphaAnimation(1f, 0.5f)

        val animationSet = AnimationSet(true)
        animationSet.duration = 200
        animationSet.interpolator= CycleInterpolator(1f)
        animationSet.addAnimation(scaleAnimation)
        animationSet.addAnimation(alphaAnimation)
        animationSet.setFillAfter(true)

        for (item in channelViews.value) {
            item.startAnimation(animationSet)
        }
    }
}










