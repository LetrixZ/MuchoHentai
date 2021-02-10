package com.letrix.muchohentai.app.di

import android.content.Context
import androidx.room.Room
import com.letrix.muchohentai.app.room.AppDatabase
import com.letrix.muchohentai.app.room.cover.CoverDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideCoverDao(appDatabase: AppDatabase): CoverDao {
        return appDatabase.coverDao()
    }
}