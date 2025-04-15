package com.example.captioncraft.data.local.dao

import androidx.room.*
import com.example.captioncraft.data.local.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM Post WHERE userId IN (SELECT followeeId FROM Follow WHERE followerId = :currentUserId) ORDER BY createdAt DESC")
    fun getFeedPosts(currentUserId: Int): Flow<List<PostEntity>>

    @Query("SELECT * FROM Post WHERE userId IN (SELECT followeeId FROM Follow WHERE followerId = :currentUserId) ORDER BY createdAt DESC")
    suspend fun getAllFeedPostsOnce(currentUserId: Int): List<PostEntity>

    @Transaction
    suspend fun replaceAllFeedPosts(currentUserId: Int, newPosts: List<PostEntity>) {
        deleteFeedPosts(currentUserId)
        insertPosts(newPosts)
    }

    @Query("DELETE FROM Post WHERE userId IN (SELECT followeeId FROM Follow WHERE followerId = :currentUserId)")
    suspend fun deleteFeedPosts(currentUserId: Int)

    @Query("SELECT * FROM Post WHERE userId = :userId ORDER BY createdAt DESC")
    fun getUserPosts(userId: Int): Flow<List<PostEntity>>

    @Query("SELECT * FROM Post WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getUserPostsOnce(userId: Int): List<PostEntity>

    @Transaction
    suspend fun replaceUserPosts(userId: Int, posts: List<PostEntity>) {
        deleteUserPosts(userId)
        insertPosts(posts)
    }

    @Query("DELETE FROM Post WHERE userId = :userId")
    suspend fun deleteUserPosts(userId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

}
