package com.octopus.android.multimedia.fragments

import com.airbnb.mvrx.fragmentViewModel
import kotlinx.coroutines.delay
import java.lang.Exception

//usb视频播放页面
class USBFragment : VideoListFragment() {
    override val viewModel: USBViewModel by fragmentViewModel()
}

class USBViewModel(initialState: VideoListState) : VideoListViewModel(initialState) {
    override fun fetchData() {
        suspend {

            //TODO 获取usb视频列表数据

            //模拟出错场景
            delay(3000)
            throw Exception("test exception")
            // listOf("1", "2", "3")
        }.execute { copy(list = it) }
    }
}