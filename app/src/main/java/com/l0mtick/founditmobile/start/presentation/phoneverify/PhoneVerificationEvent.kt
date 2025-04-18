package com.l0mtick.founditmobile.start.presentation.phoneverify

sealed interface PhoneVerificationEvent {
    data class OpenTelegramBot(val fullPhoneNumber: String) : PhoneVerificationEvent
    data object OnVerificationSuccess : PhoneVerificationEvent
}