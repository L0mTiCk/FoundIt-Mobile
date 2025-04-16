package com.l0mtick.founditmobile.start.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.ValidationManager
import com.l0mtick.founditmobile.common.presentation.util.UiText
import com.l0mtick.founditmobile.common.presentation.util.asUiText
import com.l0mtick.founditmobile.common.presentation.util.isValid
import com.l0mtick.founditmobile.common.presentation.util.updateAndValidateField
import com.l0mtick.founditmobile.start.domain.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val validator: ValidationManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

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

            LoginAction.LoginFormAction.OnSubmit -> {
                loginSubmit()
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
                signupSubmit()
            }
        }
    }

    private fun loginSubmit() {
        val current = _state.value
        if (current !is LoginState.LoginForm) return

        val loginState = current.loginState
        val passwordState = current.passwordState

        val states = listOf(loginState, passwordState)

        val isValid = states.all { it.isValid() }

        if (isValid) {
            viewModelScope.launch {
                _state.update {
                    current.copy(
                        isLoading = true
                    )
                }
                val result = authRepository.login(
                    loginValue = loginState.value,
                    password = passwordState.value
                )
                when (result) {
                    is Result.Success -> {
                        eventChannel.send(LoginEvent.LoginSuccess)
                        Log.i("auth_flow", "Logged In!!")
                        return@launch
                    }

                    is Result.Error -> {
                        eventChannel.send(LoginEvent.Error(
                            result.error.asUiText()
                        ))
                        Log.e("auth_flow", "Error: ${result.error}")
                    }
                }
                _state.update {
                    current.copy(
                        isLoading = false
                    )
                }
            }
        } else {
            viewModelScope.launch {
                eventChannel.send(LoginEvent.Error(
                    UiText.StringResource(R.string.empty_field)
                ))
            }
            Log.e("auth_flow", "Smth not valid")
        }
    }

    private fun signupSubmit() {
        val current = _state.value
        if (current !is LoginState.SignupForm) return

        val loginState = current.loginState
        val emailState = current.emailState
        val passwordState = current.passwordState
        val confirmPasswordState = current.confirmPasswordState

        val states = listOf(loginState, emailState, passwordState, confirmPasswordState)

        val isValid =
            states.all { it.isValid() }
                    && passwordState.value == confirmPasswordState.value

        if (isValid) {
            viewModelScope.launch {
                _state.update {
                    current.copy(
                        isLoading = true
                    )
                }
                val result = authRepository.register(
                    username = loginState.value,
                    email = emailState.value,
                    password = current.passwordState.value
                )
                when (result) {
                    is Result.Success -> {
                        Log.i("auth_flow", "Signed Up!!")
                        _state.update {
                            LoginState.LoginForm(loginState = loginState)
                        }
                        return@launch
                    }

                    is Result.Error -> {
                        eventChannel.send(LoginEvent.Error(
                            result.error.asUiText()
                        ))
                        Log.e("auth_flow", "Error: ${result.error}")
                    }
                }
                _state.update {
                    current.copy(
                        isLoading = false
                    )
                }
            }
        } else {
            viewModelScope.launch {
                eventChannel.send(LoginEvent.Error(
                    UiText.StringResource(R.string.passwords_do_not_match)
                ))
            }
            Log.e("auth_flow", "Smth not valid")
        }
    }
}

