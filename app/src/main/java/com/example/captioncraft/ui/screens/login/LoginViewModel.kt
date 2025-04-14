package com.example.captioncraft.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    private val _loginStatus = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginStatus: StateFlow<LoginUiState> = _loginStatus.asStateFlow()

    fun onUsernameChanged(newValue: String) {
        username = newValue
    }

    fun onPasswordChanged(newValue: String) {
        password = newValue
    }

    fun login() {
        if (username == "admin" && password == "admin") {
            _loginStatus.value = LoginUiState.Success
        } else {
            _loginStatus.value = LoginUiState.Error("Invalid credentials")
        }
    }

    fun register() {

    }
}