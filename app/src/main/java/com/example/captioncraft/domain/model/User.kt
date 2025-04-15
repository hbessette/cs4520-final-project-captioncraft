package com.example.captioncraft.domain.model

import java.util.Date

data class User(
    val id: Int,
    val username: String,
    val name: String,
    val avatarUrl: String?,
    val createdAt: Date?
)