package com.l0mtick.founditmobile.start.presentation.phoneverify

sealed interface PhoneVerificationEvent {
    data object OpenTelegramBot : PhoneVerificationEvent
}