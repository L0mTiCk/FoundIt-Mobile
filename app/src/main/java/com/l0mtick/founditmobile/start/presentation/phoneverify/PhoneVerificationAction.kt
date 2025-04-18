package com.l0mtick.founditmobile.start.presentation.phoneverify

sealed interface PhoneVerificationAction {
    data class OnPhoneNumberChanged(val newPhone: String) : PhoneVerificationAction
    data class OnCountryPicked(val phoneCode: String, val countryLang: String) : PhoneVerificationAction
    data object OnOpenTelegramClick : PhoneVerificationAction

    data class OnMoveToCode(val fullPhoneNumber: String) : PhoneVerificationAction
    data object OnMoveToPhoneNumber : PhoneVerificationAction

    data class OnOtpChange(val value: String, val isOtpFilled: Boolean) : PhoneVerificationAction
}