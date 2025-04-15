package com.example.captioncraft.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.captioncraft.data.local.entity.PostEntity
import com.example.captioncraft.data.repository.CaptionRepository
import com.example.captioncraft.data.repository.PostRepository
import com.example.captioncraft.data.repository.UserRepository
import com.example.captioncraft.domain.model.Caption
import com.example.captioncraft.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val captionRepository: CaptionRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val feedPosts: StateFlow<List<Post>> = userRepository.currentUser
        .filterNotNull()
        .flatMapLatest { postRepository.getFeedPosts(it.id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addCaption(postId: Int, text: String) {
        viewModelScope.launch {
            val userId = userRepository.currentUser.value?.id ?: return@launch
            captionRepository.addCaption(postId, userId, text)
        }
    }

    fun toggleLike(captionId: Int) {
        viewModelScope.launch {
            val userId = userRepository.currentUser.value?.id ?: return@launch
            captionRepository.toggleLike(captionId, userId, captionRepository.isLikedByUser(captionId, userId))
        }
    }

    fun getCaptions(postId: Int): Flow<List<Caption>> = captionRepository.getCaptionsForPost(postId)
} 