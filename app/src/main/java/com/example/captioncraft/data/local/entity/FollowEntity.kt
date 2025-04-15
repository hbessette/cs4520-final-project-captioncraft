package com.example.captioncraft.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "Follow",
    primaryKeys = ["followerId", "followeeId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["followerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["followeeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("followerId"), Index("followeeId")]
)
data class FollowEntity(
    val followerId: Int,
    val followeeId: Int
)

