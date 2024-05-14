package com.octopus.android.multimedia.fragments.music

import com.airbnb.mvrx.fragmentViewModel
import com.zhuchao.android.fbase.DataID
import com.zhuchao.android.fbase.FileUtils
import com.zhuchao.android.fbase.ObjectList
import com.zhuchao.android.fbase.bean.DirBean
import com.zhuchao.android.session.Cabinet
import com.zhuchao.android.session.MApplication
import com.zhuchao.android.session.TPlayManager
import java.io.File


//音乐Folder页面
class MusicFolderFragment : MediaFolderFragment() {
    override val viewModel: MusicFolderViewModel by fragmentViewModel()

}

class MusicFolderViewModel(initialState: MediaFolderState) : MediaGroupViewModel(initialState) {

    override fun updatePlayListAndPlay(url: String) = withState {
        if (!it.key.isNullOrEmpty()) {

            val list = FileUtils.getDirMediaFiles(
                it.key,
                DataID.MEDIA_TYPE_ID_AUDIO
            )
            if (list != null) {
                val videoList = Cabinet.getPlayManager().transformToVideoList(list)
                Cabinet.getPlayManager().createPlayingListOrder(it.key,videoList)
            }

            TPlayManager.getInstance(MApplication.getAppContext()).startPlay(url)
        }
    }

    override fun fetchFolderDataSync(): List<MediaFolder>? {
        val objectList1 = FileUtils.getAllSubMediaDirList(
            MApplication.getAppContext(),
            "/",

            DataID.MEDIA_TYPE_ID_AllDIR,
            DataID.MEDIA_TYPE_ID_AUDIO
        )

        return objectList1?.all?.mapNotNull {
            val item = it.value
            if (item is DirBean) {
                MediaFolder(
                    name = "${item.name}(${item.subItemCount})",
                    path = item.path
                )
            } else {
                null
            }
        }
    }


    override fun fetchMediaDataSync(key: String): List<MediaFolder>? {

        val list = FileUtils.getDirMediaFiles(
            key,
            DataID.MEDIA_TYPE_ID_AUDIO
        )

        return list.mapNotNull { MediaFolder(name = File(it).name, path = it) }


    }

}