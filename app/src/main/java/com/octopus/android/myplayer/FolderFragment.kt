package com.octopus.android.myplayer

import com.airbnb.mvrx.fragmentViewModel

//文件夹视频列表页面
class FolderFragment : VideoListFragment() {
    private val viewModel: FolderViewModel by fragmentViewModel()
    override fun providerViewModel(): VideoListViewModel {
        return viewModel
    }
}

class FolderViewModel(initialState: VideoListState) : VideoListViewModel(initialState) {
    override fun fetchData() {
        suspend {

            //TODO 获取文件夹视频列表数据

            listOf("1", "2", "3")
        }.execute { copy(list = it) }
    }
}