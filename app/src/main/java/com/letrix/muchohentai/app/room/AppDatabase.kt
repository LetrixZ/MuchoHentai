package com.letrix.muchohentai.app.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.letrix.muchohentai.app.room.cover.CoverDao
import com.letrix.muchohentai.app.room.cover.Cover

@Database(
    entities = [Cover::class],
    version = 1,
    exportSchema = false
)
@TypeConverters()
abstract class AppDatabase : RoomDatabase() {

    abstract fun coverDao(): CoverDao

    companion object {
        val DATABASE_NAME: String = "app_database"
    }


}