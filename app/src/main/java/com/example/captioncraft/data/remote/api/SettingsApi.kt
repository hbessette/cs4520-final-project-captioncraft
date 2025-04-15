package com.example.captioncraft.data.remote.api

import com.example.captioncraft.data.remote.dto.SettingsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface SettingsApi {
    @PUT("users/{userId}/settings")
    suspend fun updateSettings(
        @Path("userId") userId: Int,
        @Body settings: SettingsDto
    ): SettingsDto

    @GET("users/{userId}/settings")
    suspend fun getSettings(@Path("userId") userId: Int): SettingsDto
}