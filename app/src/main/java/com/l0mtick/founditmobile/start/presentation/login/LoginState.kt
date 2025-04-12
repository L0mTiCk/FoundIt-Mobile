package com.l0mtick.founditmobile.start.presentation.login

sealed class LoginState {
    data object Initial : LoginState()

    data class LoginForm(
        val loginValue: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    ): LoginState()

    data class SignupForm(
        val login: String = "",
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    ): LoginState()
}