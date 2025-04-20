package com.l0mtick.founditmobile.start.data.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.start.domain.repository.AuthApi
import com.l0mtick.founditmobile.start.domain.repository.AuthRepository

class AuthRepositoryImpl(private val localStorage: LocalStorage, private val authApi: AuthApi): AuthRepository {

    private val emailRegex = Regex("^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])\$")

    override suspend fun login(loginValue: String, password: String): Result<Unit, DataError> {
        val result = if (emailRegex.matches(loginValue)) {
            authApi.loginByEmail(loginValue, password)
        } else {
            authApi.loginByLogin(loginValue, password)
        }
        when (result) {
            is Result.Success -> {
                localStorage.setToken(result.data.token)
                localStorage.setRefreshToken(result.data.refreshToken)
                localStorage.setEmail(result.data.email)
                localStorage.setUsername(result.data.username)
                localStorage.setIsLoggedIn(true)
                return Result.Success(Unit)
            }
            is Result.Error -> return Result.Error(result.error)
        }
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<Unit, DataError> {
        return authApi.register(username, email, password)
    }

    override suspend fun verifyPhone(phone: String, code: String): Result<Unit, DataError> {
        return authApi.verifyPhone(phone, code)
    }

    override suspend fun checkAvailability(username: String, email: String): Result<Unit, DataError> {
        val usernameCheckResult = authApi.checkUsernameAvailability(username)
        if (usernameCheckResult is Result.Error) {
            return Result.Error(usernameCheckResult.error)
        }

        val emailCheckResult = authApi.checkEmailAvailability(email)
        if (emailCheckResult is Result.Error) {
            return Result.Error(emailCheckResult.error)
        }

        return Result.Success(Unit)
    }
}