package com.l0mtick.founditmobile.start.data.remote.api

import com.l0mtick.founditmobile.common.data.remote.api.BaseApiService
import com.l0mtick.founditmobile.common.data.remote.request.PushTokenRequest
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.ConnectivityObserver
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.start.data.remote.request.PhoneVerifyRequest
import com.l0mtick.founditmobile.start.data.remote.request.UserLoginRequest
import com.l0mtick.founditmobile.start.data.remote.request.UserRegisterRequest
import com.l0mtick.founditmobile.start.data.remote.response.GuestTokenResponse
import com.l0mtick.founditmobile.start.data.remote.response.UserLoginResponse
import com.l0mtick.founditmobile.start.domain.repository.AuthApi
import io.ktor.client.HttpClient

class AuthApiImpl(
    httpClient: HttpClient,
    localStorage: LocalStorage,
    connectivityObserver: ConnectivityObserver
) :
    BaseApiService(httpClient, localStorage, connectivityObserver), AuthApi {
    override suspend fun loginByEmail(
        email: String,
        password: String
    ): Result<UserLoginResponse, DataError.Network> {
        val request = UserLoginRequest(email, password)
        return post(
            path = "auth/login/email",
            body = request
        )
    }

    override suspend fun loginByLogin(
        login: String,
        password: String
    ): Result<UserLoginResponse, DataError.Network> {
        val request = UserLoginRequest(login, password)
        return post(
            path = "auth/login/username",
            body = request
        )
    }

    override suspend fun loginAsGuest(): Result<GuestTokenResponse, DataError.Network> {
        return get("auth/login/guest")
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<Unit, DataError.Network> {
        val request = UserRegisterRequest(username, password, email)
        return post(
            path = "auth/register/user",
            body = request
        )
    }

    override suspend fun verifyPhone(phone: String, code: String): Result<Unit, DataError.Network> {
        val request = PhoneVerifyRequest(phone, code)
        return post(
            path = "auth/verify/phone",
            body = request
        )
    }

    override suspend fun checkUsernameAvailability(username: String): Result<Unit, DataError.Network> {
        return get(
            path = "auth/check-username",
            params = { append("username", username) }
        )
    }

    override suspend fun checkEmailAvailability(email: String): Result<Unit, DataError.Network> {
        return get(
            path = "auth/check-email",
            params = { append("email", email) }
        )
    }

    override suspend fun checkToken(): Result<Unit, DataError.Network> {
        return getAuth(
            path = "auth/verify-token",
        )
    }

    override suspend fun sendUserPushToken(token: String): Result<Unit, DataError.Network> {
        val request = PushTokenRequest(token)
        return postAuth(
            path = "user/push-token",
            body = request
        )
    }

}