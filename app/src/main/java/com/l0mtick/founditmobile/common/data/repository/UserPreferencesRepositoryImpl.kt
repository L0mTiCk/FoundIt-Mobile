package com.l0mtick.founditmobile.common.data.repository

import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.common.domain.repository.UserPreferencesRepository

class UserPreferencesRepositoryImpl(
    private val localStorage: LocalStorage
) : UserPreferencesRepository {

    override suspend fun isDarkThemeEnabled(): Boolean {
        return localStorage.isDarkTheme()
    }

    override suspend fun setDarkTheme(isDarkTheme: Boolean) {
        localStorage.setIsDarkTheme(isDarkTheme)
    }

    override suspend fun areNotificationsEnabled(): Boolean {
        return localStorage.areNotificationsEnabled()
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        localStorage.setAreNotificationsEnabled(enabled)
    }

    override suspend fun logOut() {
        localStorage.clear()
    }
}