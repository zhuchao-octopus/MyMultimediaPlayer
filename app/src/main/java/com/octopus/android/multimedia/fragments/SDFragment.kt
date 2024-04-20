package com.octopus.android.multimedia.fragments

import com.airbnb.mvrx.fragmentViewModel
import kotlinx.coroutines.delay

//sd卡视频列表页面
class SDFragment : VideoListFragment() {
    override val viewModel: SDViewModel by fragmentViewModel()
}

class SDViewModel(initialState: VideoListState) : VideoListViewModel(initialState) {
    override fun fetchData() {
        suspend {
            //TODO 获取SD卡视频列表数据
            //模拟加载耗时场景
            delay(5000)
            listOf("1", "2", "3")
        }.execute { copy(list = it) }
    }
}