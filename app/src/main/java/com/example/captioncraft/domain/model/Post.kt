package com.example.captioncraft.domain.model

import java.util.Date

data class Post(
    val id: Int,
    val userId: Int,
    val image: String,
    val caption: String?,
    val createdAt: Date,
    val likes: Int,
    val topCaptionId: Int?
)
