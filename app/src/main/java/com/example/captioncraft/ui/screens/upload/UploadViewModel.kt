package com.example.captioncraft.ui.screens.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.captioncraft.data.repository.PostRepository
import com.example.captioncraft.data.repository.UserRepository
import com.example.captioncraft.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    fun uploadImage(imageUri: Uri?) {
        val post = Post(
            id = 0,
            userId = userRepository.currentUser.value!!.id,
            image = imageUri.toString(),
            caption = "",
            createdAt = Date(),
            likes = 0,
            topCaptionId = null
        )
        viewModelScope.launch {
            postRepository.uploadPost(post)
        }
    }
} 