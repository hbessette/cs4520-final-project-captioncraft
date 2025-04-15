package com.example.captioncraft.data.repository

import com.example.captioncraft.data.local.dao.CaptionDao
import com.example.captioncraft.data.remote.api.CaptionApi
import com.example.captioncraft.data.remote.dto.CaptionCreateDto
import com.example.captioncraft.domain.mapper.toDomain
import com.example.captioncraft.domain.mapper.toEntity
import com.example.captioncraft.domain.model.Caption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class CaptionRepository @Inject constructor(
    private val captionDao: CaptionDao,
    private val captionApi: CaptionApi
) {
    fun getCaptionsForPost(postId: Int): Flow<List<Caption>> = flow {
        captionDao.getCaptionsByPost(postId).map { list -> list.map { it.toDomain() } }
    }

    suspend fun addCaption(postId: Int, userId: Int, text: String) : Result<Caption> {
        val caption = CaptionCreateDto(
            postId,
            userId,
            text
        )
        return try {
            val response = captionApi.createCaption(caption)
            Result.success(response.toDomain())
        } catch (e : Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleLike(captionId: Int, userId: Int, isCurrentlyLiked: Boolean) {
        if (isCurrentlyLiked) {
            captionApi.unlikeCaption(captionId, userId)
        } else {
            captionApi.likeCaption(captionId, userId)
        }
    }

    suspend fun isLikedByUser(captionId: Int, userId: Int): Boolean {
        return captionApi.isCaptionLikedByUser(captionId, userId)
    }

    suspend fun getLikesCount(captionId: Int): Int {
        return captionApi.getCaptionLikes(captionId)
    }
}
