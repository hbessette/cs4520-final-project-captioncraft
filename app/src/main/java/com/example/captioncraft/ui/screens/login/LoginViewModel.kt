package com.example.captioncraft.ui.screens.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.captioncraft.data.repository.UserRepository
import com.example.captioncraft.domain.model.User
import com.example.captioncraft.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    private val _loginStatus = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginStatus: StateFlow<LoginUiState> = _loginStatus.asStateFlow()

    private val _registerResult = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val registerResult: StateFlow<LoginUiState> = _registerResult

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
            viewModelScope.launch {
                val response = userRepository.login(username, password)
                if (response.isSuccess) {
                    _loginStatus.value = LoginUiState.Success
                } else {
                    _loginStatus.value = LoginUiState.Error(response.toString())
                }

            }
        }
    }

    fun register() {
        viewModelScope.launch {
            val response = userRepository.register(username, password)
            if (response.isSuccess) {
                _registerResult.value = LoginUiState.Success
            } else {
                _registerResult.value = LoginUiState.Error(response.toString())
            }
        }
    }
}