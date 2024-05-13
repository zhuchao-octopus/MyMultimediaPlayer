package com.octopus.android.multimedia.fragments.music;

import com.airbnb.mvrx.fragmentViewModel
import com.zhuchao.android.session.Cabinet
import com.zhuchao.android.video.OMedia
import kotlinx.coroutines.delay

//音乐播放列表页面
class MusicPlayListFragment : MusicListFragment() {
    override val viewModel: MusicPlayListViewModel by fragmentViewModel()

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }
}

class MusicPlayListViewModel(initialState: MusicListState) : VideoListViewModel(initialState) {
    override fun fetchData() {
        suspend {
            //获取播放列表数据
            val result =
                Cabinet.getPlayManager().playingHistoryList.all.values.toList() as List<OMedia>
            //播放列表需要过滤音频文件
            result?.filter { it.isAudio }
        }.execute { copy(list = it) }
    }

    override fun updatePlayList() {
        Cabinet.getPlayManager().playingHistoryList.updateLinkOrder()
    }
}
