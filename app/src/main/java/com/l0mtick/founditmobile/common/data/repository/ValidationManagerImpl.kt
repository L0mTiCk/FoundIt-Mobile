package com.l0mtick.founditmobile.common.data.repository

import android.util.Patterns
import com.l0mtick.founditmobile.common.domain.error.ValidationError
import com.l0mtick.founditmobile.common.domain.model.ValidationResult
import com.l0mtick.founditmobile.common.domain.model.ValidationRule
import com.l0mtick.founditmobile.common.domain.repository.ValidationManager

class ValidationManagerImpl: ValidationManager {

    private val simpleCharacterRegex = Regex("^[a-zA-Z0-9_]+$")
    private val defaultTextRegex = Regex("^[\\p{L}\\p{N} .,!?\"'()\\[\\]{}:;\\-+/=*&%#@\$€£₽№°§±|\\n\\r]+$")
    private val emailValidator = Validator(
        listOf(
            ValidationRule(ValidationError.EMAIL_INVALID) {
                Patterns.EMAIL_ADDRESS.matcher(it).matches()
            }
        )
    )

    private val usernameValidator = Validator(
        listOf(
            ValidationRule(ValidationError.USERNAME_TOO_SHORT) { it.length >= 4 },
            ValidationRule(ValidationError.USERNAME_INVALID_CHARACTERS) {
                it.matches(simpleCharacterRegex)
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

    private val loginOrEmailValidator = object : Validator(emptyList()) {
        override fun validate(input: String): ValidationResult {
            if (input.length < 4) {
                return ValidationResult(isValid = false, errorTypes = listOf(ValidationError.USERNAME_TOO_SHORT))
            }

            val isEmail = input.contains("@")

            return if (isEmail) {
                if (Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                    ValidationResult(isValid = true, emptyList())
                } else {
                    ValidationResult(isValid = false, listOf(ValidationError.EMAIL_INVALID))
                }
            } else {
                if (input.matches(simpleCharacterRegex)) {
                    ValidationResult(isValid = true, emptyList())
                } else {
                    ValidationResult(isValid = false, listOf(ValidationError.USERNAME_INVALID_CHARACTERS))
                }
            }
        }
    }

    private val itemTitleValidator = Validator(
        listOf(
            ValidationRule(ValidationError.EMPTY) { it.isNotEmpty() },
            ValidationRule(ValidationError.TOO_MUCH_SYMBOLS) { it.length <= 64 },
            ValidationRule(ValidationError.INVALID_CHARACTERS) { it.matches(defaultTextRegex) }
        )
    )

    private val itemDescriptionValidator = Validator(
        listOf(
            ValidationRule(ValidationError.EMPTY) { it.isNotEmpty() },
            ValidationRule(ValidationError.TOO_MUCH_SYMBOLS) { it.length <= 255 },
            ValidationRule(ValidationError.INVALID_CHARACTERS) { it.matches(defaultTextRegex) }
        )
    )

    override fun validateEmail(input: String): ValidationResult = emailValidator.validate(input)

    override fun validateUsername(input: String): ValidationResult = usernameValidator.validate(input)

    override fun validatePassword(input: String): ValidationResult = passwordValidator.validate(input)

    override fun validateFieldEmptiness(input: String): ValidationResult = emptyValidator.validate(input)

    override fun validateUsernameOrEmail(input: String): ValidationResult = loginOrEmailValidator.validate(input)

    override fun validateItemTitle(input: String): ValidationResult = itemTitleValidator.validate(input)

    override fun validateItemDescription(input: String): ValidationResult = itemDescriptionValidator.validate(input)
}

private open class Validator(private val rules: List<ValidationRule>) {
    open fun validate(input: String): ValidationResult {
        val errors = rules.filterNot { it.validate(input) }.map { it.errorType }
        return ValidationResult(errors.isEmpty(), errors)
    }
}