package com.example.captioncraft.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.captioncraft.data.models.Post
import com.example.captioncraft.data.models.User
import com.example.captioncraft.data.repository.LocalRepository
import com.example.captioncraft.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: LocalRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = repository.currentUser

    val userPosts: StateFlow<List<Post>> = currentUser
        .map { user ->
            user?.let { repository.getUserPosts(it.id) } ?: emptyList()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun logout(navController: NavHostController) {
        navController.navigate(Screen.Login.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        repository.logout()
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