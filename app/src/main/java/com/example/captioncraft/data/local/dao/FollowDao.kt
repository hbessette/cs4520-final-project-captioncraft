package com.example.captioncraft.data.local.dao

import androidx.room.*
import com.example.captioncraft.data.local.entity.FollowEntity
import com.example.captioncraft.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun followUser(follow: FollowEntity)

    @Delete
    suspend fun unfollowUser(follow: FollowEntity)

    @Query("SELECT * FROM User WHERE id IN (SELECT followeeId FROM Follow WHERE followerId = :userId)")
    fun getFollowing(userId: Int): Flow<List<UserEntity>>

    @Query("SELECT * FROM User WHERE id IN (SELECT followerId FROM Follow WHERE followeeId = :userId)")
    fun getFollowers(userId: Int): Flow<List<UserEntity>>

    @Query("SELECT COUNT(*) FROM Follow WHERE followeeId = :userId")
    fun getFollowerCount(userId: Int): Flow<Int>

    @Query("SELECT COUNT(*) FROM Follow WHERE followerId = :userId")
    fun getFollowingCount(userId: Int): Flow<Int>

    @Query("SELECT followeeId FROM Follow WHERE followerId = :userId")
    suspend fun getFollowingIds(userId: Int): List<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM Follow WHERE followerId = :currentUserId AND followeeId = :targetUserId)")
    fun isFollowingOnce(currentUserId: Int, targetUserId: Int): Boolean
}
