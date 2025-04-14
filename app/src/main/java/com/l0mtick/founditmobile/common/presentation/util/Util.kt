package com.l0mtick.founditmobile.common.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.common.domain.model.ValidationResult
import com.l0mtick.founditmobile.start.presentation.login.LoginState
import kotlinx.coroutines.launch

inline fun <reified T : LoginState> ViewModel.updateAndValidateField(
    crossinline getField: (T) -> TextFieldState,
    crossinline setField: (T, TextFieldState) -> T,
    newValue: String,
    noinline validate: suspend (String) -> ValidationResult,
    crossinline getState: () -> LoginState,
    crossinline updateState: (LoginState) -> Unit
) {
    val current = getState()
    if (current !is T) return

    val updatedField = getField(current).copy(value = newValue)
    updateState(setField(current, updatedField))

    viewModelScope.launch {
        val validation = validate(newValue)

        val currentValidated = getState()
        if (currentValidated !is T) return@launch

        val validatedField = getField(currentValidated).copy(
            isError = !validation.isValid,
            errors = validation.errorTypes
        )
        updateState(setField(currentValidated, validatedField))
    }
}