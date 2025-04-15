package com.example.captioncraft.data.local.dao

import androidx.room.*
import com.example.captioncraft.data.local.entity.UserEntity
import com.example.captioncraft.domain.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM User WHERE id = :id")
    fun getUserById(id: Int): UserEntity?

    @Update
    suspend fun updateUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("SELECT * FROM User WHERE username LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%'")
    fun searchUsers(query: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM User WHERE username LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%'")
    suspend fun searchUsersOnce(query: String): List<UserEntity>

}
