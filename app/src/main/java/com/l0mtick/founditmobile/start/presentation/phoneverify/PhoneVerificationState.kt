package com.l0mtick.founditmobile.start.presentation.phoneverify

sealed class PhoneVerificationState {

    data object PhoneEnter : PhoneVerificationState()

    data object CodeVerify : PhoneVerificationState()

}