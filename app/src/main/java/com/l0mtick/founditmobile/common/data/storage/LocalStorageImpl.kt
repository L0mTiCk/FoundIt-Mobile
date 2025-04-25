package com.l0mtick.founditmobile.common.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import kotlinx.coroutines.flow.first
import java.util.Locale

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

    override suspend fun setUsername(username: String) {
        context.dataStore.edit { it[PreferencesKeys.USERNAME] = username }
    }

    override suspend fun getUsername(): String? {
        return context.dataStore.data.first()[PreferencesKeys.USERNAME]
    }

    override suspend fun setAppLanguage(languageCode: String) {
        context.dataStore.edit {
            it[PreferencesKeys.APP_LANGUAGE] = languageCode
        }
    }

    override suspend fun getAppLanguage(): String {
        val stored = context.dataStore.data.first()[PreferencesKeys.APP_LANGUAGE]
        return stored ?: Locale.getDefault().language
    }

    override suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}