package com.octopus.android.multimedia.fragments.music

import com.airbnb.mvrx.fragmentViewModel
import com.zhuchao.android.fbase.DataID
import com.zhuchao.android.fbase.FileUtils
import com.zhuchao.android.fbase.ObjectList
import com.zhuchao.android.session.MApplication
import java.io.File


//音乐Folder页面
class MusicFolderFragment : MediaFolderFragment() {
    override val viewModel: MusicFolderViewModel by fragmentViewModel()

}

class MusicFolderViewModel(initialState: MediaFolderState) : MediaGroupViewModel(initialState) {
    override fun fetchFolderDataSync(): List<MediaFolder>? {
        val objectList1 = ObjectList()
        val objectList2 = ObjectList()
        FileUtils.getAllSubMediaDirList(
            MApplication.getAppContext(),
            "/",
            objectList1, objectList2,
            DataID.MEDIA_TYPE_ID_AllDIR,
            DataID.MEDIA_TYPE_ID_AUDIO
        )
        //objectList1.printAll()
        //objectList2.printAll()
        return objectList1?.all?.map { MediaFolder(name = File(it.key).name, path = it.key) }
    }


    override fun fetchMediaDataSync(key: String): List<MediaFolder>? {
        val objectList1 = ObjectList()
        val objectList2 = ObjectList()
        FileUtils.getAllSubMediaDirList(
            MApplication.getAppContext(),
            key,
            objectList1,
            objectList2,
            DataID.MEDIA_TYPE_ID_SUBDIR,
            DataID.MEDIA_TYPE_ID_AUDIO
        )
//        objectList1.printAll()
//        objectList2.printAll()
        return objectList2?.all?.map { MediaFolder(name = it.key, path = it.key) }
    }

}