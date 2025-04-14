package com.example.captioncraft.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.captioncraft.data.models.Post
import com.example.captioncraft.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    val posts: StateFlow<List<Post>> = repository.getFeedPosts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addCaption(postId: String, text: String) {
        repository.addCaption(postId, text)
    }

    fun toggleCaptionLike(postId: String, captionId: String) {
        repository.toggleCaptionLike(postId, captionId)
    }
} 