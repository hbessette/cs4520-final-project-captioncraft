package com.example.captioncraft.data.local.dao

import androidx.room.*
import com.example.captioncraft.data.local.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Query("SELECT * FROM Post WHERE id = :id")
    fun getPostById(id: Int): Flow<PostEntity?>

    @Query("SELECT * FROM Post WHERE userId = :userId ORDER BY createdAt DESC")
    fun getPostsByUser(userId: Int): Flow<List<PostEntity>>

    @Query("SELECT * FROM Post ORDER BY createdAt DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Update
    suspend fun updatePost(post: PostEntity)

    @Delete
    suspend fun deletePost(post: PostEntity)

    @Query("SELECT * FROM Post WHERE userId IN (:userIds) ORDER BY createdAt DESC")
    fun getPostsByUsers(userIds: List<Int>): Flow<List<PostEntity>>
}
