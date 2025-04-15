package com.example.captioncraft.data.repository

import com.example.captioncraft.data.local.dao.FollowDao
import com.example.captioncraft.data.local.dao.PostDao
import com.example.captioncraft.data.remote.api.PostApi
import com.example.captioncraft.domain.mapper.toDomain
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
    fun getFeedPosts(currentUserId : Int) : Flow<List<Post>> = flow {
        val followingIds = followDao.getFollowingIds(currentUserId)
        emitAll(
            postDao.getPostsByUsers(followingIds)
                .map { list -> list.map { it.toDomain() } }
        )
    }

    fun addCaption(postId: String, text: String) {

    }

    fun toggleCaptionLike(postId: String, captionId: String) {

    }

}