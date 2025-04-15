package com.example.captioncraft.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.captioncraft.data.local.dao.CaptionDao
import com.example.captioncraft.data.local.dao.FollowDao
import com.example.captioncraft.data.local.dao.PostDao
import com.example.captioncraft.data.local.dao.SettingsDao
import com.example.captioncraft.data.local.dao.UserDao
import com.example.captioncraft.data.local.entity.CaptionEntity
import com.example.captioncraft.data.local.entity.FollowEntity
import com.example.captioncraft.data.local.entity.PostEntity
import com.example.captioncraft.data.local.entity.SettingsEntity
import com.example.captioncraft.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, PostEntity::class, CaptionEntity::class, FollowEntity::class, SettingsEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun captionDao(): CaptionDao
    abstract fun followDao(): FollowDao
    abstract fun settingsDao(): SettingsDao
}
