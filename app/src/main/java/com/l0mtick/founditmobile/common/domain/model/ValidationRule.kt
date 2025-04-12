package com.l0mtick.founditmobile.common.domain.model

import com.l0mtick.founditmobile.common.domain.error.ValidationError

data class ValidationRule(
    val errorType: ValidationError,
    val validate: (String) -> Boolean
)
