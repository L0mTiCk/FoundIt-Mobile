package com.l0mtick.founditmobile.common.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.common.domain.model.ValidationResult
import com.l0mtick.founditmobile.start.presentation.login.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

inline fun <reified T : LoginState> ViewModel.updateAndValidateLoginField(
    crossinline getField: (T) -> TextFieldState,
    crossinline setField: (T, TextFieldState) -> T,
    newValue: String,
    noinline validate: suspend (String) -> ValidationResult,
    crossinline getState: () -> LoginState,
    crossinline updateState: (LoginState) -> Unit
) {
    val current = getState()
    if (current !is T) return

    val updatedField = getField(current).copy(value = newValue.trim())
    updateState(setField(current, updatedField))

    viewModelScope.launch {
        val validation = validate(updatedField.value)

        val currentValidated = getState()
        if (currentValidated !is T) return@launch

        val validatedField = getField(currentValidated).copy(
            isError = !validation.isValid,
            errors = validation.errorTypes
        )
        updateState(setField(currentValidated, validatedField))
    }
}

inline fun <reified S> ViewModel.updateAndValidateTextFieldInState(
    stateFlow: MutableStateFlow<S>,
    crossinline getField: (S) -> TextFieldState,
    crossinline setField: (S, TextFieldState) -> S,
    newValue: String,
    noinline validate: suspend (String) -> ValidationResult
) {
    val current = stateFlow.value

    val updatedField = getField(current).copy(value = newValue.trim())
    stateFlow.update { setField(it, updatedField) }

    viewModelScope.launch {
        val result = validate(updatedField.value)

        val validatedField = getField(stateFlow.value).copy(
            isError = !result.isValid,
            errors = result.errorTypes
        )
        stateFlow.update { setField(it, validatedField) }
    }
}