package com.octopus.android.multimedia.fragments.video

import com.airbnb.mvrx.fragmentViewModel
import com.zhuchao.android.fbase.MMLog
import com.zhuchao.android.fbase.MessageEvent
import com.zhuchao.android.fbase.MethodThreadMode
import com.zhuchao.android.fbase.TCourierSubscribe
import com.zhuchao.android.fbase.eventinterface.EventCourierInterface
import com.zhuchao.android.session.Cabinet
import com.zhuchao.android.video.OMedia

//usb视频播放页面
class USBFragment : VideoListFragment() {

    override val viewModel: USBViewModel by fragmentViewModel()

    @TCourierSubscribe(threadMode = MethodThreadMode.threadMode.MAIN)
    fun onTCourierSubscribeEvent(courierInterface: EventCourierInterface) {
        MMLog.d("USBFragment", "onTCourierSubscribeEvent:${courierInterface.toStr()}")



        when (courierInterface.id) {
            //USB 媒体库更新后,刷新列表页面
            MessageEvent.MESSAGE_EVENT_USB_VIDEO -> {
                viewModel.fetchData()
            }

            //设备移除后,显示空数据
            MessageEvent.MESSAGE_EVENT_USB_UNMOUNT -> {
                viewModel.clearData()
            }

        }
    }
}

class USBViewModel(initialState: VideoListState) : VideoListViewModel(initialState) {

    override fun fetchData() {
        suspend {
            val result =
                Cabinet.getPlayManager().localUSBMediaVideos.all.values.toList() as List<OMedia>
            MMLog.d("USBFragment", "fetchData:${result?.size}")
            result
        }.execute { copy(list = it) }
    }


    fun clearData() {
        suspend {
            emptyList<OMedia>()
        }.execute { copy(list = it) }
    }
}