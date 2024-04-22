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
    fun onTCourierSubscribeEvent(courierInterface: EventCourierInterface) {
        MMLog.d(tag, "onTCourierSubscribeEvent:$courierInterface")
        when (courierInterface.id) {
            //USB 媒体库更新后,刷新列表页面
            MessageEvent.MESSAGE_EVENT_USB_VIDEO -> {
                viewModel.fetchData()
                //Cabinet.getPlayManager().playingList.add(Cabinet.getPlayManager().localUSBMediaVideos);
            }
        }
    }
}

class USBViewModel(initialState: VideoListState) : VideoListViewModel(initialState) {

    override fun fetchData() {
        suspend {
            Cabinet.getPlayManager().localUSBMediaVideos.all.values.toList() as List<OMedia>
        }.execute { copy(list = it) }
    }

}