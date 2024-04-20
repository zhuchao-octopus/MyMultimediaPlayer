package com.octopus.android.multimedia.fragments;

import com.airbnb.mvrx.fragmentViewModel
import kotlinx.coroutines.delay

//播放列表页面
class PlayListFragment : VideoListFragment() {


    override val viewModel: PlayListViewModel by fragmentViewModel()


}

class PlayListViewModel(initialState: VideoListState) : VideoListViewModel(initialState) {
    override fun fetchData() {
        suspend {

            //TODO 获取播放列表数据
            delay(3000)
            listOf("/storage/emulated/0/Movies/1665669595.mp4", "2", "3","4","5","6")
        }.execute { copy(list = it) }
    }
}