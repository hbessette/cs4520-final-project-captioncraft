package com.example.captioncraft.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.captioncraft.data.local.entity.UserEntity
import com.example.captioncraft.data.repository.FollowRepository
import com.example.captioncraft.data.repository.UserRepository
import com.example.captioncraft.domain.model.Post
import com.example.captioncraft.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

    val searchResults: StateFlow<List<User>> = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) flowOf(emptyList())
            else userRepository.searchUsers(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFollow(targetUserId: Int) {
        val currentUserId = userRepository.currentUser.value!!.id
        val isFollowing = followRepository.isFollowing(currentUserId, targetUserId)
        if (isFollowing) {
            unfollowUser(currentUserId, targetUserId)
        } else {
            followUser(currentUserId, targetUserId)
        }

    }

    private fun followUser(currentUserId: Int, targetUserId: Int) {
        viewModelScope.launch {
            followRepository.follow(currentUserId, targetUserId)
        }
    }

    private fun unfollowUser(currentUserId: Int, targetUserId: Int) {
        viewModelScope.launch {
            followRepository.unfollow(currentUserId, targetUserId)
        }
    }
} 