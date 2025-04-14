package com.l0mtick.founditmobile.start.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.common.domain.repository.ValidationManager
import com.l0mtick.founditmobile.common.presentation.util.updateAndValidateField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class LoginViewModel(private val validator: ValidationManager) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow<LoginState>(LoginState.Initial)
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginState.Initial
        )

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnMoveToLogin -> {
                _state.update {
                    LoginState.LoginForm()
                }
            }

            LoginAction.OnMoveToSignup -> {
                _state.update {
                    LoginState.SignupForm()
                }
            }

            is LoginAction.LoginFormAction -> handleLoginFormAction(action)
            is LoginAction.SignupFormAction -> handleSignupFormAction(action)
        }
    }

    private fun handleLoginFormAction(action: LoginAction.LoginFormAction) {
        val current = _state.value as? LoginState.LoginForm ?: return

        with(current) {
            when (action) {
                is LoginAction.LoginFormAction.OnLoginChanged -> {
                    updateAndValidateField<LoginState.LoginForm>(
                        getField = { it.loginState },
                        setField = { state, field -> state.copy(loginState = field) },
                        newValue = action.value,
                        validate = validator::validateUsername,
                        getState = { _state.value },
                        updateState = { newState -> _state.update { newState } }
                    )
                }

                is LoginAction.LoginFormAction.OnPasswordChanged -> {
                    updateAndValidateField<LoginState.LoginForm>(
                        getField = { it.passwordState },
                        setField = { state, field -> state.copy(passwordState = field) },
                        newValue = action.value,
                        validate = validator::validatePassword,
                        getState = { _state.value },
                        updateState = { newState -> _state.update { newState } }
                    )
                }
                LoginAction.LoginFormAction.OnSubmit -> { /*TODO*/ }
            }
        }
    }

    private fun handleSignupFormAction(action: LoginAction.SignupFormAction) {
        val current = _state.value
        if (current !is LoginState.SignupForm) return

        when (action) {
            is LoginAction.SignupFormAction.OnConfirmPasswordChanged -> {
                updateAndValidateField<LoginState.SignupForm>(
                    getField = { it.confirmPasswordState },
                    setField = { state, field -> state.copy(confirmPasswordState = field) },
                    newValue = action.value,
                    validate = validator::validatePassword,
                    getState = { _state.value },
                    updateState = { newState -> _state.update { newState } }
                )
            }
            is LoginAction.SignupFormAction.OnEmailChanged -> {
                updateAndValidateField<LoginState.SignupForm>(
                    getField = { it.emailState },
                    setField = { state, field -> state.copy(emailState = field) },
                    newValue = action.value,
                    validate = validator::validateEmail,
                    getState = { _state.value },
                    updateState = { newState -> _state.update { newState } }
                )
            }
            is LoginAction.SignupFormAction.OnPasswordChanged -> {
                updateAndValidateField<LoginState.SignupForm>(
                    getField = { it.passwordState },
                    setField = { state, field -> state.copy(passwordState = field) },
                    newValue = action.value,
                    validate = validator::validatePassword,
                    getState = { _state.value },
                    updateState = { newState -> _state.update { newState } }
                )
            }
            is LoginAction.SignupFormAction.OnUsernameChanged -> {
                updateAndValidateField<LoginState.SignupForm>(
                    getField = { it.loginState },
                    setField = { state, field -> state.copy(loginState = field) },
                    newValue = action.value,
                    validate = validator::validateUsername,
                    getState = { _state.value },
                    updateState = { newState -> _state.update { newState } }
                )
            }
            LoginAction.SignupFormAction.OnSubmit -> {

            }
        }
    }
}

