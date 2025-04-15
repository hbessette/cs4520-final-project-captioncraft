package com.example.captioncraft.domain.mapper

import com.example.captioncraft.data.local.entity.FollowEntity
import com.example.captioncraft.data.remote.dto.FollowDto
import com.example.captioncraft.domain.model.Follow

fun FollowDto.toDomain() = Follow(followerId, followeeId)

fun Follow.toEntity() = FollowEntity(followerId, followeeId)

fun FollowEntity.toDomain() = Follow(followerId, followeeId)