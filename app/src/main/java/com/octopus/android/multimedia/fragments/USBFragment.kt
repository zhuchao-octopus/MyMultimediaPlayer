package com.octopus.android.multimedia.fragments

import com.airbnb.mvrx.fragmentViewModel
import com.zhuchao.android.session.Cabinet
import kotlinx.coroutines.delay

//usb视频播放页面
class USBFragment : VideoListFragment() {
    override val viewModel: USBViewModel by fragmentViewModel()
}

class USBViewModel(initialState: VideoListState) : VideoListViewModel(initialState) {
    override fun fetchData() {
        suspend {

            //TODO 获取usb视频列表数据

            //模拟出错场景
            //delay(3000)
            //throw Exception("test exception")
            // listOf("1", "2", "3")
            Cabinet.getPlayManager().localUSBMediaVideos.all.values.toList()

        }.execute { copy(list = it) }
    }
}