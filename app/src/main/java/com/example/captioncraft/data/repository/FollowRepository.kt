package com.example.captioncraft.data.repository

import com.example.captioncraft.data.local.dao.FollowDao
import com.example.captioncraft.data.local.dao.UserDao
import com.example.captioncraft.data.local.entity.FollowEntity
import com.example.captioncraft.data.remote.api.FollowApi
import com.example.captioncraft.data.remote.dto.FollowDto
import com.example.captioncraft.domain.mapper.toDomain
import com.example.captioncraft.domain.mapper.toEntity
import com.example.captioncraft.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FollowRepository @Inject constructor(
    private val followDao: FollowDao,
    private val followApi: FollowApi,
    private val userDao: UserDao
) {
    fun getFollowers(userId: Int): Flow<List<User>> =
        followDao.getFollowers(userId).map { it.map { user -> user.toDomain() } }

    fun getFollowing(userId: Int): Flow<List<User>> =
        followDao.getFollowing(userId).map { it.map { user -> user.toDomain() } }

    fun getFollowerCount(userId: Int): Flow<Int> = followDao.getFollowerCount(userId)

    fun getFollowingCount(userId: Int): Flow<Int> = followDao.getFollowingCount(userId)

    suspend fun syncFollowers(userId: Int) {
        try {
            val remote = followApi.getFollowers(userId).map { it.toDomain() }
            val local = followDao.getFollowers(userId).first().map { it.toDomain() }

            if (remote != local) {
                userDao.insertUsers(remote.map { it.toEntity() })
            }
        } catch (_: Exception) { }
    }

    suspend fun syncFollowing(userId: Int) {
        try {
            val remote = followApi.getFollowing(userId).map { it.toDomain() }
            val local = followDao.getFollowing(userId).first().map { it.toDomain() }

            if (remote != local) {
                userDao.insertUsers(remote.map { it.toEntity() })
            }
        } catch (_: Exception) { }
    }

    suspend fun follow(currentUserId: Int, targetUserId: Int) {
        followApi.followUser(FollowDto(followerId = currentUserId, followeeId = targetUserId))
        followDao.followUser(FollowEntity(currentUserId, targetUserId))
    }

    suspend fun unfollow(currentUserId: Int, targetUserId: Int) {
        followApi.unfollowUser(FollowDto(followerId = currentUserId, followeeId = targetUserId))
        followDao.unfollowUser(FollowEntity(currentUserId, targetUserId))
    }

    fun isFollowing(currentUserId: Int, targetUserId: Int) : Boolean {
        return followDao.isFollowingOnce(currentUserId, targetUserId)
    }
}