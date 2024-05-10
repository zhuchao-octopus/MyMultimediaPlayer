package com.octopus.android.multimedia.fragments.music

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import com.airbnb.mvrx.fragmentViewModel
import com.zhuchao.android.fbase.MMLog
import com.zhuchao.android.fbase.MessageEvent
import com.zhuchao.android.fbase.MethodThreadMode
import com.zhuchao.android.fbase.TCourierSubscribe
import com.zhuchao.android.fbase.eventinterface.EventCourierInterface
import com.zhuchao.android.session.Cabinet
import com.zhuchao.android.video.OMedia
import kotlinx.coroutines.delay

//音乐Collection页面
class MusicCollectionFragment : MusicListFragment() {
    override val viewModel: MusicCollectionViewModel by fragmentViewModel()


}

class MusicCollectionViewModel(initialState: MusicListState) : VideoListViewModel(initialState) {
    override fun fetchData() {
        suspend {
            //获取SD卡视频列表数据
            Cabinet.getPlayManager().allMusic as List<OMedia>
        }.execute { copy(list = it) }
    }
}








