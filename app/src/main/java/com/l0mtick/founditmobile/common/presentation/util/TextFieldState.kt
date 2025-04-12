package com.l0mtick.founditmobile.common.presentation.util

import androidx.compose.runtime.Immutable
import com.l0mtick.founditmobile.common.domain.error.ValidationError

@Immutable
data class TextFieldState(
    val value: String = "",
    val isError: Boolean = false,
    val errors: List<ValidationError> = emptyList()
)