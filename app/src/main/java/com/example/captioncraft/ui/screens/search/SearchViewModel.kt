package com.example.captioncraft.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.captioncraft.data.local.entity.UserEntity
import com.example.captioncraft.data.repository.LocalRepository
import com.example.captioncraft.data.repository.UserRepository
import com.example.captioncraft.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val searchResults: StateFlow<List<User>> = searchQuery
        .debounce(300)
        .map { query ->
            if (query.isBlank()) {
                emptyList()
            } else {
                userRepository.searchUsers(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFollow(userId: String) {
        repository.toggleFollow(userId)
    }
} 