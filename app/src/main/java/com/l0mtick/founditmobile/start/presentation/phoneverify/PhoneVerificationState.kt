package com.l0mtick.founditmobile.start.presentation.phoneverify

sealed class PhoneVerificationState {

    data class PhoneEnter(
        val phoneCode: String = "+375",
        val phoneNumber: String = "",
        val defaultLang: String = "by",
        val isValidPhone: Boolean = false
    ) : PhoneVerificationState()

    data class CodeVerify(
        val fullPhoneNumber: String,
        val otpValue: String = "",
        val isOtpFilled: Boolean = false,
        val isLoading: Boolean = false
    ) : PhoneVerificationState()

}