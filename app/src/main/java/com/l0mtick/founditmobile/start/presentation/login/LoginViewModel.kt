package com.l0mtick.founditmobile.start.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class LoginViewModel() : ViewModel() {

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
        }
    }

}