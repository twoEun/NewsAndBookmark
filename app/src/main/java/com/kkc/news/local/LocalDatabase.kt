package com.kkc.news.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kkc.news.local.roomData.BookMark
import com.kkc.news.local.service.BookMarkService

@Database(entities = [BookMark::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun bookmarkService(): BookMarkService

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "bookmark_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}