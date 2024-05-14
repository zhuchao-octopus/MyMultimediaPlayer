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
import com.zhuchao.android.session.TPlayManager
import com.zhuchao.android.video.OMedia
import kotlinx.coroutines.delay

//音乐Artists页面
class MusicArtistsFragment : MediaFolderFragment() {
    override val viewModel: MusicArtistsViewModel by fragmentViewModel()
}


class MusicArtistsViewModel(initialState: MediaFolderState) : MediaGroupViewModel(initialState) {


    override fun updatePlayListAndPlay(url: String) = withState {
        if (!it.key.isNullOrEmpty()) {
            val list =
                TPlayManager.getInstance(MApplication.getAppContext()).allMusic.getMusicByArtist(it.key)
            if (list != null) {
                Cabinet.getPlayManager().createPlayingListOrder(it.key,list)
            }
            TPlayManager.getInstance(MApplication.getAppContext()).startPlay(url)
        }
    }

    override fun fetchFolderDataSync(): List<MediaFolder>? {
        return TPlayManager.getInstance(MApplication.getAppContext()).artistList.all?.map {
            MediaFolder(
                name = it.key,
                path = it.key
            )
        }
    }

    override fun fetchMediaDataSync(key: String): List<MediaFolder>? {
        return TPlayManager.getInstance(MApplication.getAppContext()).allMusic.getMusicByArtist(key)
            .toList()
            .map { MediaFolder(name = it.name, path = it.srcUrl) }
    }

}