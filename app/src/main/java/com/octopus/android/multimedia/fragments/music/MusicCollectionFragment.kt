package com.octopus.android.multimedia.fragments.music

import com.airbnb.mvrx.fragmentViewModel
import com.zhuchao.android.session.Cabinet
import com.zhuchao.android.session.MApplication
import com.zhuchao.android.session.TPlayManager

//音乐Collection页面
class MusicCollectionFragment : MusicListFragment() {
    override val viewModel: MusicCollectionViewModel by fragmentViewModel()

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }
}

class MusicCollectionViewModel(initialState: MusicListState) : VideoListViewModel(initialState) {
    override fun fetchData() {
        suspend {
            //获取SD卡视频列表数据
//            MediaRoomDatabase.getDatabase(MApplication.getAppContext())
//                .provideUserCollectionDao().queryAll().mapNotNull {
////                    TPlayManager.getInstance(MApplication.getAppContext())
////                        .getOMediaFromPlayLists(it.path)
//                    OMedia(it.path)
//                }
            TPlayManager.getInstance(MApplication.getAppContext()).favouriteList.toOMediaList()

        }.execute { copy(list = it) }
    }

    override fun updatePlayList() {

        Cabinet.getPlayManager().createPlayingListOrder("favourite", Cabinet.getPlayManager().favouriteList)
    }
}