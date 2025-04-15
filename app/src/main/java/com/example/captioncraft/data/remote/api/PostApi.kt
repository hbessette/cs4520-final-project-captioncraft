package com.example.captioncraft.data.remote.api

import com.example.captioncraft.data.remote.dto.PostDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApi {
    @GET("users/{userId}/feed-posts")
    suspend fun getFeedPosts(@Path("userId") userId: Int): List<PostDto>

    @GET("users/{userId}/posts")
    suspend fun getPostsByUser(@Path("userId") userId: Int): List<PostDto>

    @POST("posts")
    suspend fun uploadPost(@Body post: PostDto): PostDto
}