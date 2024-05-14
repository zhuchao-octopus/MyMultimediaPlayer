//package com.octopus.android.multimedia.room
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//@Database(entities = [UserCollection::class], version = 1)
//abstract class MediaRoomDatabase : RoomDatabase() {
//    abstract fun provideUserCollectionDao(): UserCollectionDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: MediaRoomDatabase? = null
//        fun getDatabase(context: Context): MediaRoomDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    MediaRoomDatabase::class.java,
//                    "media_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }
//}