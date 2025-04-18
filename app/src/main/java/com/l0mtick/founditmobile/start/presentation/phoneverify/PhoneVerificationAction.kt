package com.l0mtick.founditmobile.start.presentation.phoneverify

sealed interface PhoneVerificationAction {
    data class OnPhoneNumberChanged(val newPhone: String) : PhoneVerificationAction
    data class OnCountryPicked(val phoneCode: String, val countryLang: String) : PhoneVerificationAction
    data object OnOpenTelegramClick : PhoneVerificationAction
}