package com.octopus.android.multimedia.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserCollectionDao {
    @Insert
    suspend fun insert(item: UserCollection): Long

    @Delete
    suspend fun delete(item: UserCollection)

    @Query("SELECT * FROM UserCollection WHERE path=(:path)")
    suspend fun queryByPath(path: String): UserCollection?

    @Query("DELETE FROM UserCollection WHERE id=(:id)")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM UserCollection")
    suspend fun queryAll(): List<UserCollection>
}