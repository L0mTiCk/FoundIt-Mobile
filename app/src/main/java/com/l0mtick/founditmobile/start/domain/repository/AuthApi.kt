package com.l0mtick.founditmobile.start.domain.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.start.data.remote.response.GuestTokenResponse
import com.l0mtick.founditmobile.start.data.remote.response.UserLoginResponse

interface AuthApi {
    suspend fun loginByEmail(email: String, password: String): Result<UserLoginResponse, DataError.Network>
    suspend fun loginByLogin(login: String, password: String): Result<UserLoginResponse, DataError.Network>
    suspend fun loginAsGuest(): Result<GuestTokenResponse, DataError.Network>
    suspend fun register(username: String, email: String, password: String, phoneNumber: String): Result<Unit, DataError.Network>
    suspend fun verifyPhone(phone: String, code: String): Result<Unit, DataError.Network>
    suspend fun checkUsernameAvailability(username: String): Result<Unit, DataError.Network>
    suspend fun checkEmailAvailability(email: String): Result<Unit, DataError.Network>
    suspend fun checkPhoneAvailability(fullPhoneNumber: String): Result<Unit, DataError.Network>
    suspend fun checkToken(): Result<Unit, DataError.Network>
    suspend fun sendUserPushToken(token: String): Result<Unit, DataError.Network>
}