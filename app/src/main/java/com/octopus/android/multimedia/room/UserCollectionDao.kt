package com.octopus.android.multimedia.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface UserCollectionDao {
    @Insert
    suspend fun insert(item: UserCollection)

    @Delete
    suspend fun delete(item: UserCollection)
}