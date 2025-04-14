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

enum class LoginScreenType {
    Initial,
    Login,
    Signup
}

val LoginState.screenType: LoginScreenType
    get() = when (this) {
        LoginState.Initial -> LoginScreenType.Initial
        is LoginState.LoginForm -> LoginScreenType.Login
        is LoginState.SignupForm -> LoginScreenType.Signup
    }