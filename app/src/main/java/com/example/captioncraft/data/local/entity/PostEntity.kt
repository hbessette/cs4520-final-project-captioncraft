package com.example.captioncraft.data.local.entity

import androidx.room.*
import java.util.Date

@Entity(
    tableName = "Post",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CaptionEntity::class,
            parentColumns = ["id"],
            childColumns = ["topCaptionId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("userId"), Index("topCaptionId")]
)
data class PostEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val userId: Int,
    val image: String,
    val caption: String? = null,
    val createdAt: Date = Date(),
    val likes: Int = 0,
    val topCaptionId: Int? = null
)

