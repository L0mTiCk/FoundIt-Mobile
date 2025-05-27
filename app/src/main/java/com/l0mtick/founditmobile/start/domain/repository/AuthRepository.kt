package com.l0mtick.founditmobile.start.domain.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result

interface AuthRepository {
    suspend fun login(loginValue: String, password: String): Result<Unit, DataError>
    suspend fun loginAsGuest(): Result<Unit, DataError>
    suspend fun register(username: String, email: String, password: String, phoneNumber: String): Result<Unit, DataError>
    suspend fun verifyPhone(phone: String, code: String): Result<Unit, DataError>
    suspend fun checkAvailability(username: String, email: String): Result<Unit, DataError>
    suspend fun checkPhoneAvailability(fullPhoneNumber: String): Result<Unit, DataError.Network>
    suspend fun checkToken(): Result<Unit, DataError.Network>
}