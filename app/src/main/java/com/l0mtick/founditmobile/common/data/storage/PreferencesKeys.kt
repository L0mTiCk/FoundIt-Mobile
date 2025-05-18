package com.l0mtick.founditmobile.common.data.storage

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val TOKEN = stringPreferencesKey("token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val PUSH_TOKEN = stringPreferencesKey("push_token")
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    val EMAIL = stringPreferencesKey("email")
    val USERNAME = stringPreferencesKey("username")
    val PROFILE_PICTURE_URL = stringPreferencesKey("profile_url")
    val APP_LANGUAGE = stringPreferencesKey("app_language")

}

