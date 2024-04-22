package com.octopus.android.multimedia.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentVideoPlayBinding
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.viewBinding
import com.zhuchao.android.fbase.eventinterface.PlayerStatusInfo
import com.zhuchao.android.session.TPlayManager
import com.zhuchao.android.video.OMedia


/**
 * 视频播放页面
 */
class VideoPlayFragment : BaseFragment(R.layout.fragment_video_play) {

    private val binding: FragmentVideoPlayBinding by viewBinding()
    private val viewModel: VideoPlayViewModel by fragmentViewModel()
    private lateinit var mTPlayManager: TPlayManager


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTPlayManager = TPlayManager.getInstance(context);

        mTPlayManager.apply {
            setSurfaceView(binding.surfaceView)
            callback {
                Log.d("VideoPlayFragment", "播放信息:$it")
                viewModel.setPlayStatusInfo(it)
            }
        }


        //点击列表
        binding.ivList.setOnClickListenerWithInterval {
            findNavController().popBackStack()
        }


        //点击播放
        binding.viewPlay.setOnClickListenerWithInterval {
          //  Log.d("test22")
            withState(viewModel) {

                mTPlayManager.apply {
                    mTPlayManager.playPause();
                    /*if (isPlaying) {
                                    mTPlayManager?.stopPlay()
                                    viewModel.setPlayState(false)
                                } else {
                                    startPlay(it.media)
                                    //toastLong("开始播放${it.url}")
                                    viewModel.setPlayState(true)
                                }*/
                }
            }
        }
        //点击播放区域,改变按钮状态
        binding.viewPlay.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                binding.ivPlay.isPressed = true
            } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                binding.ivPlay.isPressed = false
            }
            false
        }

        binding.viewPrev.setOnClickListenerWithInterval {
            mTPlayManager?.playPre()
        }
        binding.viewNext.setOnClickListenerWithInterval {
            mTPlayManager?.playNext()
        }

        binding.ivEq.setOnClickListenerWithInterval {
            //TODO 点击eq
        }

        binding.progressView.visibility = View.GONE

        withState(viewModel) {
            mTPlayManager.apply {
                startPlay(it.media)
                viewModel.setPlayState(true)
            }
        }
    }

    override fun invalidate() = withState(viewModel) {
        //根据播放信息,改变UI
        if (it.playStatusInfo == null) {
            binding.progressView.visibility = View.GONE
        } else {
            binding.progressView.visibility = View.VISIBLE //显示进度和时间
            binding.tvCurrentPlayTime.text =
                convertMillisToTime(it.playStatusInfo.timeChanged) //设置当前时间
            binding.tvVideoTime.text = convertMillisToTime(it.playStatusInfo.length)    //设置总时长

            if (it.playStatusInfo.length > 0) {
                val percentage =
                    it.playStatusInfo.timeChanged / it.playStatusInfo.length.toFloat() * 100
                binding.ivProgress.progress = percentage.toInt()
            } else {
                binding.ivProgress.progress = 0
            }
        }

        if (it.playing) {
            binding.ivPlay.setImageResource(R.drawable.selector_stop)
        } else {
            binding.ivPlay.setImageResource(R.drawable.selector_play)
        }

    }

    override fun onResume() {
        super.onResume()

        //恢复播放

        //mTPlayManager.resumePlay()
    }

    override fun onStop() {
        super.onStop()

        //暂停播放
        //mTPlayManager.stopPlay()
    }


    private fun convertMillisToTime(millis: Long): String {
        val seconds = millis / 1000
        val hours = seconds / 3600
        val minutes = seconds % 3600 / 60
        val remainingSeconds = seconds % 60
        if (hours == 0L) {
            return String.format("%02d:%02d", minutes, remainingSeconds)
        }
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }


}

data class VideoPlayState(
    val playing: Boolean = false,//播放中
    val playStatusInfo: PlayerStatusInfo? = null,//播放状态信息
    val media: OMedia? = null  //文件url
) : MavericksState {
    constructor(args: OMedia?) : this(media = args)
}

class VideoPlayViewModel(
    initialState: VideoPlayState
) : MavericksViewModel<VideoPlayState>(initialState) {

    /**
     * 切换播放状态
     * */
    fun togglePlayState() = withState {
        setState { copy(playing = !it.playing) }
    }

    fun setPlayState(playing: Boolean) = withState {
        setState { copy(playing = playing) }
    }

    /**
     * 设置播放状态信息
     * */
    fun setPlayStatusInfo(info: PlayerStatusInfo) {
        //PlayerStatusInfo 不支持clone ,这里手动拷贝
        val result = PlayerStatusInfo()
        result.obj = info.obj
        result.eventCode = info.eventCode
        result.eventType = info.eventType
        result.position = info.position
        result.timeChanged = info.timeChanged
        result.lengthChanged = info.lengthChanged
        result.positionChanged = info.positionChanged
        result.changedType = info.changedType
        result.surfaceW = info.surfaceW
        result.surfaceH = info.surfaceH
        result.videoW = info.videoW
        result.videoH = info.videoH
        result.volume = info.volume
        result.playRate = info.playRate
        result.length = info.length
        result.lastError = info.lastError
        setState { copy(playStatusInfo = result) }
    }

}

