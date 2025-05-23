package com.l0mtick.founditmobile.common.domain.repository

interface UserPreferencesRepository {

    suspend fun isDarkThemeEnabled(): Boolean

    suspend fun setDarkTheme(isDarkTheme: Boolean)
    
    suspend fun areNotificationsEnabled(): Boolean
    
    suspend fun setNotificationsEnabled(enabled: Boolean)

    suspend fun logOut()
}