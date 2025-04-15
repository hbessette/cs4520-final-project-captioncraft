package com.example.captioncraft.data.remote.api

import com.example.captioncraft.data.remote.dto.RegisterDto
import com.example.captioncraft.data.remote.dto.UserDto
import retrofit2.http.*

interface UserApi {

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserDto

    @POST("users")
    suspend fun createUser(@Body user: UserDto): UserDto

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UserDto): UserDto

    @GET("users")
    suspend fun getAllUsers(): List<UserDto>

    @POST("users/register")
    suspend fun register(@Body request: RegisterDto): UserDto

    @POST("users/login")
    suspend fun authenticate(@Body request: RegisterDto): UserDto?
}
