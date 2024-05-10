package com.octopus.android.multimedia.fragments.music

import com.airbnb.mvrx.fragmentViewModel
import com.zhuchao.android.fbase.DataID
import com.zhuchao.android.fbase.FileUtils
import com.zhuchao.android.fbase.MMLog
import com.zhuchao.android.fbase.MessageEvent
import com.zhuchao.android.fbase.MethodThreadMode
import com.zhuchao.android.fbase.ObjectList
import com.zhuchao.android.fbase.TCourierSubscribe
import com.zhuchao.android.fbase.eventinterface.EventCourierInterface
import com.zhuchao.android.session.Cabinet
import com.zhuchao.android.session.MApplication
import com.zhuchao.android.video.OMedia
import kotlinx.coroutines.delay

//音乐Artists页面
class MusicArtistsFragment : MediaFolderFragment() {
    override val viewModel: MusicArtistsViewModel by fragmentViewModel()
}


class MusicArtistsViewModel(initialState: MediaFolderState) : MediaGroupViewModel(initialState) {
    override fun fetchFolderDataSync(): List<MediaFolder>? {
        return Cabinet.getPlayManager().artistList.all?.map { MediaFolder(name = it.key, path = it.key) }
    }

    override fun fetchMediaDataSync(key: String): List<MediaFolder>? {


//
//        val objectList1 = ObjectList()
//        val objectList2 = ObjectList()
//        FileUtils.getAllSubMediaDirList(
//            MApplication.getAppContext(),
//            key,
//            objectList1,
//            objectList2,
//            DataID.MEDIA_TYPE_ID_SUBDIR,
//            DataID.MEDIA_TYPE_ID_AUDIO
//        )
//        objectList1.printAll()
//        objectList2.printAll()
//        return objectList2?.all?.map { MediaFolder(name = it.key, path = it.key) }

        return  Cabinet.getPlayManager().allMusic.getMusicByArtist(key).toList().map { MediaFolder(name = it.name, path = it.srcUrl) }
    }

}