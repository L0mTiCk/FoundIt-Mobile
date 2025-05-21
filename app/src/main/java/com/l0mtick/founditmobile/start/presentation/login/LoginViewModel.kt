package com.l0mtick.founditmobile.start.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.UserSessionManager
import com.l0mtick.founditmobile.common.domain.repository.ValidationManager
import com.l0mtick.founditmobile.common.presentation.util.UiText
import com.l0mtick.founditmobile.common.presentation.util.asUiText
import com.l0mtick.founditmobile.common.presentation.util.isValid
import com.l0mtick.founditmobile.common.presentation.util.updateAndValidateLoginField
import com.l0mtick.founditmobile.start.domain.repository.AuthRepository
import com.l0mtick.founditmobile.start.presentation.StartEventManager
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
    private val authRepository: AuthRepository,
    private val userSessionManager: UserSessionManager
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
            
            LoginAction.OnGuestLogin -> loginAsGuest()

            is LoginAction.LoginFormAction -> handleLoginFormAction(action)
            is LoginAction.SignupFormAction -> handleSignupFormAction(action)
        }
    }

    private fun handleLoginFormAction(action: LoginAction.LoginFormAction) {
        when (action) {
            is LoginAction.LoginFormAction.OnLoginChanged -> {
                updateAndValidateLoginField<LoginState.LoginForm>(
                    getField = { it.loginState },
                    setField = { state, field -> state.copy(loginState = field) },
                    newValue = action.value,
                    validate = validator::validateUsernameOrEmail,
                    getState = { _state.value },
                    updateState = { newState -> _state.update { newState } }
                )
            }

            is LoginAction.LoginFormAction.OnPasswordChanged -> {
                updateAndValidateLoginField<LoginState.LoginForm>(
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
                updateAndValidateLoginField<LoginState.SignupForm>(
                    getField = { it.confirmPasswordState },
                    setField = { state, field -> state.copy(confirmPasswordState = field) },
                    newValue = action.value,
                    validate = validator::validatePassword,
                    getState = { _state.value },
                    updateState = { newState -> _state.update { newState } }
                )
            }

            is LoginAction.SignupFormAction.OnEmailChanged -> {
                updateAndValidateLoginField<LoginState.SignupForm>(
                    getField = { it.emailState },
                    setField = { state, field -> state.copy(emailState = field) },
                    newValue = action.value,
                    validate = validator::validateEmail,
                    getState = { _state.value },
                    updateState = { newState -> _state.update { newState } }
                )
            }

            is LoginAction.SignupFormAction.OnPasswordChanged -> {
                updateAndValidateLoginField<LoginState.SignupForm>(
                    getField = { it.passwordState },
                    setField = { state, field -> state.copy(passwordState = field) },
                    newValue = action.value,
                    validate = validator::validatePassword,
                    getState = { _state.value },
                    updateState = { newState -> _state.update { newState } }
                )
            }

            is LoginAction.SignupFormAction.OnUsernameChanged -> {
                updateAndValidateLoginField<LoginState.SignupForm>(
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
                        Log.i("auth_flow", "Logged In!!")
                        StartEventManager.triggerEvent(StartEventManager.StartEvent.OnNavigateToMain)
                        return@launch
                    }

                    is Result.Error -> {
                        eventChannel.send(
                            LoginEvent.Error(
                                result.error.asUiText()
                            )
                        )
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
                eventChannel.send(
                    LoginEvent.Error(
                        UiText.StringResource(R.string.empty_field)
                    )
                )
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

        val isValid = states.all { it.isValid() }

        if (isValid) {
            viewModelScope.launch {
                if (passwordState.value != confirmPasswordState.value) {
                    eventChannel.send(
                        LoginEvent.Error(
                            UiText.StringResource(R.string.passwords_do_not_match)
                        )
                    )
                    return@launch
                }

                _state.update {
                    current.copy(isLoading = true)
                }

                val availabilityResult =
                    authRepository.checkAvailability(loginState.value, emailState.value)

                when (availabilityResult) {
                    is Result.Success<*, *> -> {
                        eventChannel.send(
                            LoginEvent.NavigateToPhoneVerification(
                                login = loginState.value,
                                email = emailState.value,
                                pass = passwordState.value
                            )
                        )
                    }
                    is Result.Error<*, *> -> {
                        eventChannel.send(LoginEvent.Error(availabilityResult.error.asUiText()))
                    }
                }
                _state.update {
                    current.copy(isLoading = false)
                }
            }

        }
    }

    private fun loginAsGuest() {
        viewModelScope.launch {
            when(val result = authRepository.loginAsGuest()) {
                is Result.Success -> {
                    userSessionManager.setGuestMode(true)
                    StartEventManager.triggerEvent(StartEventManager.StartEvent.OnNavigateToMainAsGuest)
                }
                is Result.Error -> {

                }
            }
        }
    }
}

