package com.l0mtick.founditmobile.start.presentation.login

sealed interface LoginAction {
    data object OnMoveToLogin : LoginAction
    data object OnMoveToSignup : LoginAction
}