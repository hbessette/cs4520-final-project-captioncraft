package com.example.captioncraft.ui.screens.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.captioncraft.data.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val repository: LocalRepository
) : ViewModel() {
    // TODO: Implement upload functionality
    // - Upload image to Firebase Storage
    // - Create post document in Firestore
    fun uploadImage(imageUri: Uri) {
        repository.createPost(imageUri.toString())
    }
} 