package com.l0mtick.founditmobile.common.domain.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Менеджер для управления сессией пользователя
 */
class UserSessionManager {
    private val _isGuestUser = MutableStateFlow(false)
    val isGuestUser: StateFlow<Boolean> = _isGuestUser.asStateFlow()
    
    /**
     * Устанавливает режим гостевого пользователя
     */
    fun setGuestMode(isGuest: Boolean) {
        _isGuestUser.value = isGuest
    }
    
    /**
     * Сбрасывает сессию пользователя
     */
    fun clearSession() {
        _isGuestUser.value = false
    }
}