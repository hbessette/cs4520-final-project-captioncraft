package com.example.captioncraft.domain.mapper

import com.example.captioncraft.data.local.entity.PostEntity
import com.example.captioncraft.data.remote.dto.PostDto
import com.example.captioncraft.domain.model.Post
import java.util.Date

fun PostDto.toDomain() = Post(
    id, userId, image, caption, parseIsoDate(created_at) ?: Date(), likes, topCaptionId
)

fun Post.toEntity() = PostEntity(
    id, userId, image, caption, createdAt, likes, topCaptionId
)

fun PostEntity.toDomain() = Post(id, userId, image, caption, createdAt, likes, topCaptionId)
