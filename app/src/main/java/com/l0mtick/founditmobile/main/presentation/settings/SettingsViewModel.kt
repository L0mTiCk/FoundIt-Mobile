package com.l0mtick.founditmobile.main.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.common.domain.repository.UserPreferencesRepository
import com.l0mtick.founditmobile.main.presentation.MainEventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            val isDarkTheme = userPreferencesRepository.isDarkThemeEnabled()
            val notificationsEnabled = userPreferencesRepository.areNotificationsEnabled()
            
            _state.update { 
                it.copy(
                    isDarkTheme = isDarkTheme,
                    notificationsEnabled = notificationsEnabled
                )
            }
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.ToggleTheme -> {
                val newThemeValue = !state.value.isDarkTheme
                _state.update { it.copy(isDarkTheme = newThemeValue) }
                viewModelScope.launch {
                    userPreferencesRepository.setDarkTheme(newThemeValue)
                    MainEventManager.triggerEvent(MainEventManager.MainEvent.OnDarkThemeChanged)
                }
            }
            SettingsAction.ToggleNotifications -> {
                val newNotificationsValue = !state.value.notificationsEnabled
                _state.update { it.copy(notificationsEnabled = newNotificationsValue) }
                viewModelScope.launch {
                    userPreferencesRepository.setNotificationsEnabled(newNotificationsValue)
                }
            }
            SettingsAction.SignOut -> {
                viewModelScope.launch {
                    userPreferencesRepository.logOut()
                }
            }
        }
    }
}