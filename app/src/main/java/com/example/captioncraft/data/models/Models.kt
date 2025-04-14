package com.example.captioncraft.data.models

data class User(
    val id: String,
    val username: String,
    val avatarUrl: String? = null,
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList()
)

data class Post(
    val id: String,
    val imageUrl: String,
    val userId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val captions: List<Caption> = emptyList()
)

data class Caption(
    val id: String,
    val text: String,
    val userId: String,
    val likes: Set<String> = emptySet(),
    val timestamp: Long = System.currentTimeMillis()
) 