package com.l0mtick.founditmobile.start.domain.repository

import com.l0mtick.founditmobile.start.data.remote.response.UserLoginResponse
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result

interface AuthApi {
    suspend fun loginByEmail(email: String, password: String): Result<UserLoginResponse, DataError.Network>
    suspend fun loginByLogin(login: String, password: String): Result<UserLoginResponse, DataError.Network>
    suspend fun register(username: String, email: String, password: String): Result<Unit, DataError.Network>
    suspend fun verifyPhone(phone: String, code: String): Result<Unit, DataError.Network>
}