package com.l0mtick.founditmobile.common.data.remote.api

import android.util.Log
import com.l0mtick.founditmobile.BuildConfig
import com.l0mtick.founditmobile.common.data.remote.request.RefreshTokenRequest
import com.l0mtick.founditmobile.common.data.remote.responses.RefreshTokenResponse
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.ParametersBuilder
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import okio.IOException

abstract class BaseApiService(
    private val httpClient: HttpClient,
    private val localStorage: LocalStorage,
) {
    val baseUrl = BuildConfig.BASE_URL + "/api"

    val defaultUnauthorizedHandler: () -> Unit = {
        Log.w("BaseApiService", "Unauthorized access")
    }

    val defaultRefreshTokenHandler: suspend () -> Boolean = {
        val token = localStorage.getRefreshToken()
        Log.e("expired_token", "Expired token trying to refresh with refresh token ${token}")
        token?.let {
            try {
                val response: RefreshTokenResponse = httpClient.post(baseUrl + "/auth/refresh") {
                    contentType(ContentType.Application.Json)
                    header("Authorization", "Bearer $it")
                    setBody(RefreshTokenRequest(it))
                }.body()
                localStorage.setToken(response.accessToken)
                localStorage.setRefreshToken(response.refreshToken)
                true
            } catch (e: Exception) {
                false
            }
        } ?: false
    }

    /**
     * Performs a GET request without authentication.
     *
     * @param T The expected type of the response body.
     * @param path The endpoint path relative to the base URL.
     * @param params Optional query parameters.
     * @param onUnauthorized Custom handler for 401 responses.
     * @return A [Result] containing either the decoded data or a [DataError.Network].
     */
    suspend inline fun <reified T> get(
        path: String,
        crossinline params: ParametersBuilder.() -> Unit = {},
        noinline onUnauthorized: () -> Unit = defaultUnauthorizedHandler
    ): Result<T, DataError.Network> =
        request(HttpMethod.Get, path, EmptyBody, params, false, onUnauthorized)

    /**
     * Performs an authenticated GET request.
     *
     * @param T The expected type of the response body.
     * @param path The endpoint path relative to the base URL.
     * @param params Optional query parameters.
     * @param onUnauthorized Custom handler for 401 responses.
     * @return A [Result] containing either the decoded data or a [DataError].
     */
    suspend inline fun <reified T> getAuth(
        path: String,
        crossinline params: ParametersBuilder.() -> Unit = {},
        noinline onUnauthorized: () -> Unit = defaultUnauthorizedHandler
    ): Result<T, DataError.Network> =
        request(HttpMethod.Get, path, EmptyBody, params, true, onUnauthorized)

    /**
     * Performs a POST request without authentication.
     *
     * @param T The expected type of the response body.
     * @param Body The type of the request body.
     * @param path The endpoint path relative to the base URL.
     * @param body The request body to send.
     * @param onUnauthorized Custom handler for 401 responses.
     * @return A [Result] containing either the decoded data or a [DataError].
     */
    suspend inline fun <reified T, reified Body> post(
        path: String,
        body: Body,
        noinline onUnauthorized: () -> Unit = defaultUnauthorizedHandler
    ): Result<T, DataError.Network> =
        request(HttpMethod.Post, path, body, {}, false, onUnauthorized)

    /**
     * Performs an authenticated POST request.
     *
     * @param T The expected type of the response body.
     * @param Body The type of the request body.
     * @param path The endpoint path relative to the base URL.
     * @param body The request body to send.
     * @param onUnauthorized Custom handler for 401 responses.
     * @return A [Result] containing either the decoded data or a [DataError].
     */
    suspend inline fun <reified T, reified Body> postAuth(
        path: String,
        body: Body,
        noinline onUnauthorized: () -> Unit = defaultUnauthorizedHandler
    ): Result<T, DataError.Network> = request(HttpMethod.Post, path, body, {}, true, onUnauthorized)

    /**
     * General-purpose request method that wraps the Ktor call,
     * applies headers, handles body and parameters, and maps known errors.
     *
     * @param T The expected type of the response body.
     * @param Body The type of the request body.
     * @param method The HTTP method to use (GET, POST, etc.).
     * @param path The endpoint path relative to the base URL.
     * @param body The request body to send, or null for methods like GET.
     * @param params Optional query parameters.
     * @param withAuth Whether to include an Authorization header.
     * @param onUnauthorized Callback for handling 401 responses.
     * @return A [Result] wrapping either the response data or a [DataError].
     */
    suspend inline fun <reified T, reified Body> request(
        method: HttpMethod,
        path: String,
        body: Body? = null,
        crossinline params: ParametersBuilder.() -> Unit = {},
        withAuth: Boolean = false,
        noinline onUnauthorized: () -> Unit = defaultUnauthorizedHandler,
        noinline refreshTokenHandler: suspend () -> Boolean = defaultRefreshTokenHandler
    ): Result<T, DataError.Network> {
        return try {
            val result = `access$httpClient`.request("$baseUrl/$path") {
                this.method = method

                if (withAuth) {
                    val token = `access$localStorage`.getToken()
                    header(HttpHeaders.Authorization, "Bearer $token")
                }

                if (body != null && body != EmptyBody) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }

                url {
                    parameters.apply(params)
                }
            }

            if (!result.status.isSuccess()) {
                val error = when (result.status) {
                    HttpStatusCode.InternalServerError -> DataError.Network.SERVER_ERROR
                    else -> DataError.Network.UNKNOWN
                }

                return Result.Error(error)
            }

            return Result.Success(result.body<T>())
        } catch (e: RedirectResponseException) {
            Result.Error(DataError.Network.UNKNOWN)
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                val refreshed = refreshTokenHandler()
                if (!refreshed) {
                    onUnauthorized()
                }
            }

            val error = when (e.response.status) {
                HttpStatusCode.BadRequest -> DataError.Network.BAD_REQUEST
                HttpStatusCode.RequestTimeout -> DataError.Network.REQUEST_TIMEOUT
                HttpStatusCode.TooManyRequests -> DataError.Network.TOO_MANY_REQUESTS
                HttpStatusCode.PayloadTooLarge -> DataError.Network.PAYLOAD_TOO_LARGE
                HttpStatusCode.Conflict -> DataError.Network.CONFLICT
                else -> DataError.Network.UNKNOWN
            }

            Result.Error(error)
        } catch (e: ServerResponseException) {
            Result.Error(DataError.Network.SERVER_ERROR)
        } catch (e: SerializationException) {
            Result.Error(DataError.Network.SERIALIZATION)
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    @PublishedApi
    internal val `access$httpClient`: HttpClient
        get() = httpClient

    @PublishedApi
    internal val `access$localStorage`: LocalStorage
        get() = localStorage
}

/**
 * Placeholder for an empty request body, used for GET requests where a body must still be passed.
 * Do not use for other needs
 */
@Serializable
data object EmptyBody
