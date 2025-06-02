package com.l0mtick.founditmobile.main.presentation.settings

sealed interface SettingsAction {
    data object ToggleTheme : SettingsAction
    data object ToggleNotifications : SettingsAction
    data object SignOut : SettingsAction
}