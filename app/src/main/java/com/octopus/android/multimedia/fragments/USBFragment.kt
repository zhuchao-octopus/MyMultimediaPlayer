package com.octopus.android.multimedia.fragments

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
    private var tag = "USBFragment";
    override val viewModel: USBViewModel by fragmentViewModel()
    @TCourierSubscribe(threadMode = MethodThreadMode.threadMode.MAIN)
    fun onTCourierSubscribeEvent(courierInterface: EventCourierInterface): Boolean {
        when (courierInterface.id) {
            MessageEvent.MESSAGE_EVENT_LOCAL_VIDEO -> {
            }

            MessageEvent.MESSAGE_EVENT_USB_VIDEO -> {
                MMLog.d(tag, "MessageEvent.MESSAGE_EVENT_USB_VIDEO");
                viewModel.fetchData()
            }

        }
        return true
    }
}

class USBViewModel(initialState: VideoListState) : VideoListViewModel(initialState) {

    override fun fetchData() {
        suspend {
            //TODO 获取usb视频列表数据
            //模拟出错场景
            //delay(3000)
            //throw Exception("test exception")
            // listOf("1", "2", "3")
            Cabinet.getPlayManager().localUSBMediaVideos.all.values.toList() as List<OMedia>
        }.execute { copy(list = it) }
    }

}