package com.example.captioncraft.ui.screens.settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.captioncraft.data.repository.UserRepository
import com.example.captioncraft.domain.model.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isNotificationsEnabled = MutableStateFlow(true)
    val isNotificationsEnabled: StateFlow<Boolean> = _isNotificationsEnabled.asStateFlow()

    private val _isPrivateModeEnabled = MutableStateFlow(false)
    val isPrivateModeEnabled: StateFlow<Boolean> = _isPrivateModeEnabled.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val settings: StateFlow<UserSettings?> = userRepository.currentUser
        .filterNotNull()
        .flatMapLatest { userRepository.observeSettings(it.id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _updateResult = MutableStateFlow<Result<UserSettings>?>(null)
    val updateResult: StateFlow<Result<UserSettings>?> = _updateResult

    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        update()
        AppCompatDelegate.setDefaultNightMode(
            if(enabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun toggleNotifications(enabled: Boolean) {
        _isNotificationsEnabled.value = enabled
        update()
    }

    fun togglePrivateMode(enabled: Boolean) {
        _isPrivateModeEnabled.value = enabled
        update()
    }



    fun sync() {
        viewModelScope.launch {
            userRepository.currentUser.value?.id?.let { userRepository.syncSettings(it) }
        }
    }

    private fun update() {
        val userSettings = userRepository.currentUser.value?.let {
            UserSettings(
                id = it.id,
                darkModeEnabled = isDarkMode.value,
                notificationsEnabled = isNotificationsEnabled.value,
                privateModeEnabled = isPrivateModeEnabled.value
            )
        }
        viewModelScope.launch {
            val userId = userRepository.currentUser.value?.id ?: return@launch
            _updateResult.value = userSettings?.let { userRepository.updateSettings(userId, it) }
        }
    }
}