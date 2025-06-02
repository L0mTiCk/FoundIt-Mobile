package com.l0mtick.founditmobile.start.presentation.login

sealed interface LoginAction {
    data object OnMoveToLogin : LoginAction
    data object OnMoveToSignup : LoginAction
    data object OnGuestLogin : LoginAction
    data object OnMoveToInitial : LoginAction

    sealed interface LoginFormAction : LoginAction {
        data class OnLoginChanged(val value: String) : LoginFormAction
        data class OnPasswordChanged(val value: String) : LoginFormAction
        data object OnSubmit : LoginFormAction
    }

    sealed interface SignupFormAction : LoginAction {
        data class OnUsernameChanged(val value: String) : SignupFormAction
        data class OnEmailChanged(val value: String) : SignupFormAction
        data class OnPasswordChanged(val value: String) : SignupFormAction
        data class OnConfirmPasswordChanged(val value: String) : SignupFormAction
        data object OnSubmit : SignupFormAction
    }
}