package com.example.captioncraft.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.captioncraft.data.local.entity.PostEntity
import com.example.captioncraft.data.local.entity.UserEntity
import com.example.captioncraft.data.repository.CaptionRepository
import com.example.captioncraft.data.repository.FollowRepository
import com.example.captioncraft.data.repository.LocalRepository
import com.example.captioncraft.data.repository.PostRepository
import com.example.captioncraft.data.repository.UserRepository
import com.example.captioncraft.domain.model.Post
import com.example.captioncraft.domain.model.User
import com.example.captioncraft.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = userRepository.currentUser

    @OptIn(ExperimentalCoroutinesApi::class)
    val userPosts: StateFlow<List<Post>> = userRepository.currentUser
        .filterNotNull()
        .flatMapLatest { postRepository.observeUserPosts(it.id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _editImageUri = MutableStateFlow<Uri?>(null)
    val editImageUri: StateFlow<Uri?> = _editImageUri

    private val _editedUsername = MutableStateFlow("")
    val editedUsername: StateFlow<String> = _editedUsername

    fun onImagePicked(uri: Uri?) {
        _editImageUri.value = uri
    }

    fun onUsernameChanged(newUsername: String) {
        _editedUsername.value = newUsername
    }

    fun enterEditMode() {
        _editedUsername.value = currentUser.value?.username ?: ""
        _editImageUri.value = null
    }

    fun updateProfile() {
        val updatedUsername = _editedUsername.value
        val updatedImageUrl = _editImageUri.value?.toString()

        val current = currentUser.value ?: return

        val updatedUser = current.copy(
            username = updatedUsername,
            avatarUrl = updatedImageUrl ?: current.avatarUrl
        )

        viewModelScope.launch {
            userRepository.updateUser(updatedUser)
        }

        _editImageUri.value = null
        _editedUsername.value = ""
    }

    fun logout(navController: NavHostController) {
        navController.navigate(Screen.Login.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        userRepository.logout()
    }

    fun navigateToSettings(navController: NavHostController) {
        navController.navigate(Screen.Settings.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
