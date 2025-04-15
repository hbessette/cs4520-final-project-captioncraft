package com.example.captioncraft.data.remote.dto

data class PostDto(
    val id: Int,
    val userId: Int,
    val image: String,
    val caption: String?,
    val created_at: String,
    val likes: Int,
    val topCaptionId: Int?,
)