package com.example.captioncraft.domain.mapper

import com.example.captioncraft.data.local.entity.CaptionEntity
import com.example.captioncraft.data.remote.dto.CaptionCreateDto
import com.example.captioncraft.data.remote.dto.CaptionDto
import com.example.captioncraft.domain.model.Caption
import java.util.Date

fun CaptionDto.toDomain() = Caption(
    id, postId, userId, text, parseIsoDate(created_at) ?: Date(), likes
)


fun Caption.toEntity() = CaptionEntity(
    id, postId, userId, text, createdAt, likes
)

fun CaptionEntity.toDomain() = Caption(id, postId, userId, text, createdAt, likes)

fun Caption.toRequestDto() = CaptionCreateDto(
    postId = postId,
    userId = userId,
    text = text
)