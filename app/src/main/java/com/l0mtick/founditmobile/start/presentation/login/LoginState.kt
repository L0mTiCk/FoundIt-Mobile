package com.l0mtick.founditmobile.start.presentation.login

import com.l0mtick.founditmobile.common.presentation.util.TextFieldState

sealed class LoginState {
    data object Initial : LoginState()

    data class LoginForm(
        val loginState: TextFieldState = TextFieldState(),
        val passwordState: TextFieldState = TextFieldState(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    ): LoginState()

    data class SignupForm(
        val loginState: TextFieldState = TextFieldState(),
        val emailState: TextFieldState = TextFieldState(),
        val passwordState: TextFieldState = TextFieldState(),
        val confirmPasswordState: TextFieldState = TextFieldState(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    ): LoginState()
}