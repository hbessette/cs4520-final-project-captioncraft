package com.example.captioncraft.data.repository

import android.util.Log
import com.bumptech.glide.util.ExceptionPassthroughInputStream
import com.example.captioncraft.data.local.dao.SettingsDao
import com.example.captioncraft.data.local.dao.UserDao
import com.example.captioncraft.data.remote.api.SettingsApi
import com.example.captioncraft.data.remote.api.UserApi
import com.example.captioncraft.data.remote.dto.RegisterDto
import com.example.captioncraft.domain.mapper.toDomain
import com.example.captioncraft.domain.mapper.toDto
import com.example.captioncraft.domain.mapper.toEntity
import com.example.captioncraft.domain.model.User
import com.example.captioncraft.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApi,
    private val settingsApi: SettingsApi,
    private val settingsDao: SettingsDao
) {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    suspend fun login(username: String, password: String): Result<User?> {
        try {
            val apiUser = userApi.authenticate(RegisterDto(username, "", password)).toDomain()
            val localUser = userDao.getUserById(apiUser.id)?.toDomain()

            if (localUser != apiUser) {
                    userDao.insertUser(apiUser.toEntity())
            }

            _currentUser.value = apiUser
            return Result.success(_currentUser.value)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    suspend fun register(username: String, name: String, password: String) : Result<User?> {
        try {
            val apiUser = userApi.register(RegisterDto(username, name, password)).toDomain()
            val localUser = userDao.getUserById(apiUser.id)?.toDomain()

            if (localUser != apiUser) {
                userDao.insertUser(apiUser.toEntity())
            }

            _currentUser.value = apiUser
            return Result.success(_currentUser.value)
        } catch (e: Exception) {
            Log.e("Network", "Error: ${e.localizedMessage}", e)
            return Result.failure(e)
        }
    }

    suspend fun updateUser(user: User) {
        try {
            userDao.updateUser(user.toEntity())
            userApi.updateUser(user.id, user.toDto())
        } catch (_ : Exception) {

        }
    }

    fun observeSettings(userId: Int): Flow<UserSettings> =
        settingsDao.getSettingsByUserId(userId)
            .filterNotNull()
            .map { it.toDomain() }


    suspend fun syncSettings(userId: Int) {
        try {
            val remote = settingsApi.getSettings(userId).toDomain()
            val local = settingsDao.getSettingsByUserIdOnce(userId)?.toDomain()

            if (remote != local) {
                settingsDao.insert(remote.toEntity())
            }
        } catch (_: Exception) { }
    }

    suspend fun updateSettings(userId: Int, newSettings: UserSettings): Result<UserSettings> {
        return try {
            val updatedFromApi = settingsApi.updateSettings(userId, newSettings.toDto()).toDomain()

            val local = settingsDao.getSettingsByUserIdOnce(userId)?.toDomain()
            if (local != updatedFromApi) {
                settingsDao.insert(updatedFromApi.toEntity())
            }

            Result.success(updatedFromApi)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun searchUsers(query: String): Flow<List<User>> = flow {
        try {
            val remote = userApi.searchUsers(query).map { it.toDomain() }
            val local = userDao.searchUsersOnce(query).map { it.toDomain() }

            if (remote != local) {
                userDao.insertUsers(remote.map { it.toEntity() })
            }
        } catch (_: Exception) { }

        emitAll(userDao.searchUsers(query).map { it.map { it.toDomain() } })
    }
}
