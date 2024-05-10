package com.octopus.android.multimedia.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 收藏表,暂时只有音乐类型
 * */
@Entity
data class UserCollection(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,//id
    val path: String,//文件路径
    val time: Long, //收藏时间
)