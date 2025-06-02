package com.l0mtick.founditmobile.start.presentation.login

import com.l0mtick.founditmobile.common.presentation.util.UiText

sealed interface LoginEvent {
    data class NavigateToPhoneVerification(val login: String, val email: String, val pass: String): LoginEvent
    data class Error(val error: UiText): LoginEvent
}