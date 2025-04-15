package com.example.captioncraft.data.repository

import com.example.captioncraft.data.local.dao.FollowDao
import com.example.captioncraft.data.local.dao.PostDao
import com.example.captioncraft.data.remote.api.PostApi
import com.example.captioncraft.domain.mapper.toDomain
import com.example.captioncraft.domain.mapper.toEntity
import com.example.captioncraft.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postDao: PostDao,
    private val followDao: FollowDao,
    private val postApi: PostApi
){
    fun observeFeedPosts(currentUserId: Int): Flow<List<Post>> =
        postDao.getFeedPosts(currentUserId).map { list -> list.map { it.toDomain() } }

    suspend fun syncFeedPosts(currentUserId: Int) {
        try {
            val apiPosts = postApi.getFeedPosts(currentUserId).map { it.toDomain() }
            val localPosts = postDao.getAllFeedPostsOnce(currentUserId)

            if (apiPosts != localPosts) {
                postDao.replaceAllFeedPosts(currentUserId, apiPosts.map { it.toEntity() })
            }
        } catch (_: Exception) {

        }
    }

    fun observeUserPosts(userId: Int): Flow<List<Post>> =
        postDao.getUserPosts(userId).map { it.map { entity -> entity.toDomain() } }

    suspend fun syncUserPosts(userId: Int) {
        try {
            val remote = postApi.getPostsByUser(userId).map { it.toDomain() }
            val local = postDao.getUserPostsOnce(userId).map { it.toDomain() }

            if (remote != local) {
                postDao.replaceUserPosts(userId, remote.map { it.toEntity() })
            }
        } catch (_: Exception) {

        }
    }

}