package com.l0mtick.founditmobile.common.domain.model

import com.l0mtick.founditmobile.common.domain.error.ValidationError

data class ValidationResult(
    val isValid: Boolean,
    val errorTypes: List<ValidationError>
)
