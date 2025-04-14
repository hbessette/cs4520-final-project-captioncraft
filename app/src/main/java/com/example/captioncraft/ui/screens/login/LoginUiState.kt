package com.example.captioncraft.ui.screens.login

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
    object Loading : LoginUiState()
}