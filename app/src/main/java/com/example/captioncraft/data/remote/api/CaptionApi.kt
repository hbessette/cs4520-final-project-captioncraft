package com.example.captioncraft.data.remote.api

import com.example.captioncraft.data.remote.dto.CaptionCreateDto
import com.example.captioncraft.data.remote.dto.CaptionDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CaptionApi {

    @GET("posts/{postId}/captions")
    suspend fun getCaptions(@Path("postId") postId: Int): List<CaptionDto>

    @POST("captions")
    suspend fun createCaption(@Body caption: CaptionCreateDto): CaptionDto

    @POST("captions/{id}/like")
    suspend fun likeCaption(@Path("id") captionId: Int, @Query("userId") userId: Int)

    @DELETE("captions/{id}/like")
    suspend fun unlikeCaption(@Path("id") captionId: Int, @Query("userId") userId: Int)

    @GET("captions/{id}/liked-by/{userId}")
    suspend fun isCaptionLikedByUser(
        @Path("id") captionId: Int,
        @Path("userId") userId: Int
    ): Boolean

    @GET("captions/{id}/likes")
    suspend fun getCaptionLikes(@Path("id") captionId: Int): Int
}
