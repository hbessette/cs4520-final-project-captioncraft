package com.example.captioncraft.data.repository

import com.bumptech.glide.util.ExceptionPassthroughInputStream
import com.example.captioncraft.data.local.dao.UserDao
import com.example.captioncraft.data.remote.api.UserApi
import com.example.captioncraft.data.remote.dto.RegisterDto
import com.example.captioncraft.domain.mapper.toDomain
import com.example.captioncraft.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApi
) {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    suspend fun login(username: String, password: String): Boolean {
        val user = userDao.authenticate(username, password)
        if (user != null) {
            _currentUser.value = user
            return true
        } else {
            val apiUser = userApi.authenticate(RegisterDto(username, password))
            if (apiUser != null) {
                _currentUser.value = apiUser.toDomain()
                return true
            }
            return false
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    suspend fun register(username: String, password: String) : Result<User> {
        return try {
            val response = userApi.register(RegisterDto(username, password))

            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
