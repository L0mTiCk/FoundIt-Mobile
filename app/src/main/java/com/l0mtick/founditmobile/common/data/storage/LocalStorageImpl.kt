package com.l0mtick.founditmobile.common.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import kotlinx.coroutines.flow.first

class LocalStorageImpl(
    private val context: Context
) : LocalStorage {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

    override suspend fun setToken(token: String) {
        context.dataStore.edit { it[PreferencesKeys.TOKEN] = token }
    }

    override suspend fun getToken(): String? {
        return context.dataStore.data.first()[PreferencesKeys.TOKEN]
    }

    override suspend fun setRefreshToken(token: String) {
        context.dataStore.edit { it[PreferencesKeys.REFRESH_TOKEN] = token }
    }

    override suspend fun getRefreshToken(): String? {
        return context.dataStore.data.first()[PreferencesKeys.REFRESH_TOKEN]
    }

    override suspend fun setIsLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.IS_LOGGED_IN] = isLoggedIn }
    }

    override suspend fun isLoggedIn(): Boolean {
        return context.dataStore.data.first()[PreferencesKeys.IS_LOGGED_IN] ?: false
    }

    override suspend fun setEmail(email: String) {
        context.dataStore.edit { it[PreferencesKeys.EMAIL] = email }
    }

    override suspend fun getEmail(): String? {
        return context.dataStore.data.first()[PreferencesKeys.EMAIL]
    }

    override suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}