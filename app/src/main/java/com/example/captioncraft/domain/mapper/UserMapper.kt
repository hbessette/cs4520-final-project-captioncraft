package com.example.captioncraft.domain.mapper

import com.example.captioncraft.data.local.entity.UserEntity
import com.example.captioncraft.data.remote.dto.UserDto
import com.example.captioncraft.domain.model.User
import java.text.SimpleDateFormat
import java.util.*

fun UserEntity.toDomain(): User = User(
    id = id,
    username = username,
    name = name,
    avatarUrl = profilePicture,
    createdAt = createdAt
)

fun UserDto.toDomain(): User = User(
    id = id,
    username = username,
    name = name,
    avatarUrl = profilePicture,
    createdAt = parseIsoDate(created_at)
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    username = username,
    name = name,
    profilePicture = avatarUrl,
    createdAt = createdAt
)

