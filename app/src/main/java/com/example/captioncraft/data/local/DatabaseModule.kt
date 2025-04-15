package com.example.captioncraft.data.local

import android.content.Context
import androidx.room.Room
import com.example.captioncraft.data.local.dao.CaptionDao
import com.example.captioncraft.data.local.dao.FollowDao
import com.example.captioncraft.data.local.dao.PostDao
import com.example.captioncraft.data.local.dao.SettingsDao
import com.example.captioncraft.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "caption_craft.db"
        ).build()
    }

    @Provides
    fun providePostDao(db: AppDatabase): PostDao = db.postDao()

    @Provides
    fun providesSettingsDao(db: AppDatabase): SettingsDao = db.settingsDao()

    @Provides
    fun providesUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun providesCaptionDao(db: AppDatabase): CaptionDao = db.captionDao()

    @Provides
    fun providesFollowDao(db: AppDatabase): FollowDao = db.followDao()
}
