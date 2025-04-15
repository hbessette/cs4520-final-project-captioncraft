package com.example.captioncraft.data.remote.api

import com.example.captioncraft.data.remote.dto.FollowDto
import com.example.captioncraft.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

interface FollowApi {
    @POST("follow")
    suspend fun followUser(@Body follow: FollowDto)

    @HTTP(method = "DELETE", path = "follow", hasBody = true)
    suspend fun unfollowUser(@Body follow: FollowDto)

    @GET("users/{userId}/followers")
    suspend fun getFollowers(@Path("userId") userId: Int): List<UserDto>

    @GET("users/{userId}/following")
    suspend fun getFollowing(@Path("userId") userId: Int): List<UserDto>
}