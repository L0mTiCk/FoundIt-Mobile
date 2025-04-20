package com.l0mtick.founditmobile.common.data.repository

import com.l0mtick.founditmobile.common.domain.error.ValidationError
import com.l0mtick.founditmobile.common.domain.model.ValidationResult
import com.l0mtick.founditmobile.common.domain.model.ValidationRule
import com.l0mtick.founditmobile.common.domain.repository.ValidationManager

class ValidationManagerImpl: ValidationManager {

    private val emailValidator = Validator(
        listOf(
            ValidationRule(ValidationError.EMAIL_INVALID) {
                it.matches(Regex("^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])\$"))
            }
        )
    )

    private val usernameValidator = Validator(
        listOf(
            ValidationRule(ValidationError.USERNAME_TOO_SHORT) { it.length >= 4 },
            ValidationRule(ValidationError.USERNAME_INVALID_CHARACTERS) {
                it.matches(Regex("^[a-zA-Z0-9_]+$"))
            }
        )
    )

    private val passwordValidator = Validator(
        listOf(
            ValidationRule(ValidationError.PASSWORD_TOO_SHORT) { it.length >= 8 },
            ValidationRule(ValidationError.PASSWORD_NO_UPPERCASE) { it.any { c -> c.isUpperCase() } },
            ValidationRule(ValidationError.PASSWORD_NO_SPECIAL_CHAR) { it.any { c -> "!@#\$%^&*()".contains(c) } }
        )
    )

    private val emptyValidator = Validator(
        listOf(
            ValidationRule(ValidationError.EMPTY) { it.isNotEmpty() }
        )
    )

    override fun validateEmail(input: String): ValidationResult = emailValidator.validate(input)

    override fun validateUsername(input: String): ValidationResult = usernameValidator.validate(input)

    override fun validatePassword(input: String): ValidationResult = passwordValidator.validate(input)

    override fun validateFieldEmptiness(input: String): ValidationResult = emptyValidator.validate(input)
}

private class Validator(private val rules: List<ValidationRule>) {
    fun validate(input: String): ValidationResult {
        val errors = rules.filterNot { it.validate(input) }.map { it.errorType }
        return ValidationResult(errors.isEmpty(), errors)
    }
}