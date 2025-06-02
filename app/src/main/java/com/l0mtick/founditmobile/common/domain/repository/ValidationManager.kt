package com.l0mtick.founditmobile.common.domain.repository

import com.l0mtick.founditmobile.common.domain.model.ValidationResult

interface ValidationManager {
    fun validateEmail(input: String): ValidationResult
    fun validateUsername(input: String): ValidationResult
    fun validatePassword(input: String): ValidationResult
    fun validateFieldEmptiness(input: String): ValidationResult
    fun validateUsernameOrEmail(input: String): ValidationResult
    fun validateItemTitle(input: String): ValidationResult
    fun validateItemDescription(input: String): ValidationResult
}