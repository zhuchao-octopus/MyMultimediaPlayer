package com.octopus.android.multimedia.fragments.video;

import com.airbnb.mvrx.fragmentViewModel
import com.zhuchao.android.session.Cabinet
import com.zhuchao.android.video.OMedia

//播放列表页面
class PlayListFragment : VideoListFragment() {
    override val viewModel: PlayListViewModel by fragmentViewModel()

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }
}

class PlayListViewModel(initialState: VideoListState) : VideoListViewModel(initialState) {
    override fun fetchData() {
        suspend {
            //获取播放列表数据
            Cabinet.getPlayManager().playingHistoryList.all.values.toList() as List<OMedia>
        }.execute { copy(list = it) }
    }
}
