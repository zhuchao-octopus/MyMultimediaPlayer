package com.octopus.android.multimedia.fragments

import com.airbnb.mvrx.fragmentViewModel
import com.zhuchao.android.fbase.MMLog
import com.zhuchao.android.fbase.MessageEvent
import com.zhuchao.android.fbase.MethodThreadMode
import com.zhuchao.android.fbase.TCourierSubscribe
import com.zhuchao.android.fbase.eventinterface.EventCourierInterface
import com.zhuchao.android.session.Cabinet
import com.zhuchao.android.video.OMedia
import kotlinx.coroutines.delay

//sd卡视频列表页面
class SDFragment : VideoListFragment() {
    override val viewModel: SDViewModel by fragmentViewModel()

    @TCourierSubscribe(threadMode = MethodThreadMode.threadMode.MAIN)
    fun onTCourierSubscribeEvent(courierInterface: EventCourierInterface) {
        MMLog.d(tag, "onTCourierSubscribeEvent:$courierInterface")
        when (courierInterface.id) {
            //媒体库更新后,刷新列表页面
            MessageEvent.MESSAGE_EVENT_SD_VIDEO -> {
                viewModel.fetchData()
            }
        }
    }

}

class SDViewModel(initialState: VideoListState) : VideoListViewModel(initialState) {
    override fun fetchData() {
        suspend {
            //获取SD卡视频列表数据
            Cabinet.getPlayManager().localSDMediaVideos.all.values.toList() as List<OMedia>
        }.execute { copy(list = it) }
    }
}