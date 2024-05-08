package com.octopus.android.multimedia.fragments.video;

import com.airbnb.mvrx.fragmentViewModel
import com.zhuchao.android.video.OMedia

//文件夹视频列表页面
class FolderFragment : VideoListFragment() {
    override val viewModel: FolderViewModel by fragmentViewModel()
}

class FolderViewModel(initialState: VideoListState) : VideoListViewModel(initialState) {
    override fun fetchData() {
        suspend {
            //TODO 获取文件夹视频列表数据
            listOf(OMedia("1"),OMedia("2"), OMedia("3"))
        }.execute { copy(list = it) }
    }
}
