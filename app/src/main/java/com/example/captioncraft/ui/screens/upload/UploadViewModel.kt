package com.example.captioncraft.ui.screens.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.captioncraft.data.repository.LocalRepository
import com.example.captioncraft.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    fun uploadImage(imageUri: Uri) {
        postRepository.createPost(imageUri.toString())
    }
} 