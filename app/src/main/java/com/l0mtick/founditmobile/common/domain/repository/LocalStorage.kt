package com.l0mtick.founditmobile.common.domain.repository

interface LocalStorage {
    suspend fun setToken(token: String)
    suspend fun getToken(): String?

    suspend fun setRefreshToken(token: String)
    suspend fun getRefreshToken(): String?

    suspend fun setIsLoggedIn(isLoggedIn: Boolean)
    suspend fun isLoggedIn(): Boolean

    suspend fun setEmail(email: String)
    suspend fun getEmail(): String?

    suspend fun setUsername(username: String)
    suspend fun getUsername(): String?

    suspend fun clear()
}