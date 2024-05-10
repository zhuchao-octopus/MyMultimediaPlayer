package com.octopus.android.multimedia.fragments.music

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.octopus.android.multimedia.R
import com.octopus.android.multimedia.databinding.FragmentMusicPlayBinding
import com.octopus.android.multimedia.fragments.BaseFragment
import com.octopus.android.multimedia.fragments.video.VideoPlayViewModel
import com.octopus.android.multimedia.utils.convertMillisToTime
import com.octopus.android.multimedia.utils.setOnClickListenerWithInterval
import com.octopus.android.multimedia.utils.viewBinding
import com.zhuchao.android.fbase.DataID
import com.zhuchao.android.fbase.PlayerStatusInfo
import com.zhuchao.android.session.TPlayManager


class MusicPlayFragment : BaseFragment(R.layout.fragment_music_play) {

    private val binding: FragmentMusicPlayBinding by viewBinding()

    private val viewModel: MusicPlayViewModel by fragmentViewModel()

    private lateinit var mTPlayManager: TPlayManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTPlayManager = TPlayManager.getInstance(context)

        mTPlayManager.apply {

            callback {
                Log.d("VideoPlayFragment", "播放信息:$it")
                viewModel.setPlayStatusInfo(it)
            }
        }


        binding.ivPlayList.setOnClickListenerWithInterval {
            //返回播放列表
            findNavController().popBackStack()
        }

        binding.viewPlay.setOnClickListenerWithInterval {
            mTPlayManager.playPause()
        }

        binding.viewPrev.setOnClickListenerWithInterval {
            mTPlayManager.playPre()
        }

        binding.viewNext.setOnClickListenerWithInterval {
            mTPlayManager.playNext()
        }

        binding.viewMode.setOnClickListenerWithInterval {
            //顺序,随机,单曲
            var target: Int
            when (mTPlayManager.playOrder) {
                DataID.PLAY_MANAGER_PLAY_ORDER2 ->
                    target = DataID.PLAY_MANAGER_PLAY_ORDER5

                DataID.PLAY_MANAGER_PLAY_ORDER5 ->
                    target = DataID.PLAY_MANAGER_PLAY_ORDER4

                else ->
                    target = DataID.PLAY_MANAGER_PLAY_ORDER2
            }
            mTPlayManager.playOrder = target

        }

        binding.viewCollection.setOnClickListenerWithInterval {
            //TODO  收藏,取消收藏
        }


    }

    override fun invalidate() = withState(viewModel) {
        Log.d("test", "invalidate:${mTPlayManager.playingMedia}")
        // 音乐名称
        binding.tvMusicName.text = mTPlayManager.playingMedia?.name
        // 演唱者名称
        binding.tvArtistsName.text = mTPlayManager.playingMedia?.movie?.artist
        // 专辑名称
        binding.tvAlbumName.text = mTPlayManager.playingMedia?.movie?.album

        //TODO 收藏状态
        binding.ivCollection.isSelected = false


        var modeImageRes: Int
        when (mTPlayManager.playOrder) {
            DataID.PLAY_MANAGER_PLAY_ORDER2 ->
                modeImageRes = R.mipmap.music_play_mode_list
            DataID.PLAY_MANAGER_PLAY_ORDER5 ->
                modeImageRes = R.mipmap.music_play_mode_shuffle
            else ->
                modeImageRes = R.mipmap.music_play_mode_loop_single
        }
        binding.ivMode.setImageResource(modeImageRes)

        //根据播放信息,改变UI
        if (it.playStatusInfo == null) {
            binding.seekBar.visibility = View.INVISIBLE
        } else {
            binding.seekBar.visibility = View.VISIBLE
            // 播放时间
            binding.tvStartTime.text = it.playStatusInfo.timeChanged.convertMillisToTime()
            // 总时间
            binding.tvEndTime.text = it.playStatusInfo.length?.convertMillisToTime()

            if (it.playStatusInfo.length > 0) {
                val percentage =
                    it.playStatusInfo.timeChanged / it.playStatusInfo.length.toFloat() * 100
                binding.seekBar.progress = percentage.toInt()
            } else {
                binding.seekBar.progress = 0
            }
        }

        if (it.playing) {
            binding.ivPlay.setImageResource(R.drawable.selector_stop)
        } else {
            binding.ivPlay.setImageResource(R.drawable.selector_play)
        }


    }
}


data class MusicPlayState(
    val playing: Boolean = false,//播放中
    val playStatusInfo: PlayerStatusInfo? = null,//播放状态信息
    val path: String? = null,  //文件path
    val uri: Uri? = null
) : MavericksState {
    constructor(args: String?) : this(path = args)
    constructor(args: Uri?) : this(uri = args)
}

class MusicPlayViewModel(
    initialState: MusicPlayState
) : MavericksViewModel<MusicPlayState>(initialState) {

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