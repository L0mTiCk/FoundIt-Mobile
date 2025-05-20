package com.l0mtick.founditmobile.common.domain.repository

interface LocalStorage {
    suspend fun setToken(token: String)
    suspend fun getToken(): String?

    suspend fun setRefreshToken(token: String)
    suspend fun getRefreshToken(): String?

    suspend fun setPushToken(token: String)
    suspend fun getPushToken(): String?

    suspend fun setIsLoggedIn(isLoggedIn: Boolean)
    suspend fun isLoggedIn(): Boolean

    suspend fun setEmail(email: String)
    suspend fun getEmail(): String?

    suspend fun setUsername(username: String)
    suspend fun getUsername(): String?

    suspend fun setProfilePictureUrl(url: String?)
    suspend fun getProfilePictureUrl(): String?

    suspend fun setAppLanguage(languageCode: String)
    suspend fun getAppLanguage(): String

    suspend fun setIsDarkTheme(isDarkTheme: Boolean)
    suspend fun isDarkTheme(): Boolean

    suspend fun setAreNotificationsEnabled(areEnabled: Boolean)
    suspend fun areNotificationsEnabled(): Boolean

    suspend fun clear()
}