package com.octopus.android.multimedia.fragments.music

import com.airbnb.mvrx.fragmentViewModel
import com.zhuchao.android.fbase.MMLog
import com.zhuchao.android.fbase.MessageEvent
import com.zhuchao.android.fbase.MethodThreadMode
import com.zhuchao.android.fbase.TCourierSubscribe
import com.zhuchao.android.fbase.eventinterface.EventCourierInterface
import com.zhuchao.android.session.Cabinet
import com.zhuchao.android.video.OMedia
import kotlinx.coroutines.delay

//音乐Folder页面
class MusicFolderFragment : MusicListFragment() {
    override val viewModel: MusicFolderViewModel by fragmentViewModel()

    @TCourierSubscribe(threadMode = MethodThreadMode.threadMode.MAIN)
    fun onTCourierSubscribeEvent(courierInterface: EventCourierInterface) {
        MMLog.d(tag, "onTCourierSubscribeEvent:$courierInterface")
        when (courierInterface.id) {
            //媒体库更新后,刷新列表页面
            MessageEvent.MESSAGE_EVENT_SD_AUDIO -> {
                viewModel.fetchData()
            }
        }
    }

}

class MusicFolderViewModel(initialState: MusicListState) : VideoListViewModel(initialState) {
    override fun fetchData() {
        suspend {
            //获取SD卡视频列表数据
            Cabinet.getPlayManager().localMediaAudios.all.values.toList() as List<OMedia>
        }.execute { copy(list = it) }
    }
}