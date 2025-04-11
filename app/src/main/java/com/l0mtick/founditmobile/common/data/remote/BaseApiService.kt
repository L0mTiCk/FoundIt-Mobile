package com.l0mtick.founditmobile.common.data.remote

import android.util.Log
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.ParametersBuilder
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import okio.IOException

abstract class BaseApiService(
    private val httpClient: HttpClient,
    private val localStorage: Any, //TODO: replace with real storage service
) {
    val baseUrl = "https://api.yourdomain.com"

    val defaultUnauthorizedHandler: () -> Unit = {
        Log.w("BaseApiService", "Unauthorized access")
    }

    suspend inline fun <reified T> get(
        path: String,
        crossinline params: ParametersBuilder.() -> Unit = {},
        noinline onUnauthorized: () -> Unit = defaultUnauthorizedHandler
    ): Result<T, DataError> = request(HttpMethod.Get, path, EmptyBody, params, false, onUnauthorized)

    suspend inline fun <reified T> getAuth(
        path: String,
        crossinline params: ParametersBuilder.() -> Unit = {},
        noinline onUnauthorized: () -> Unit = defaultUnauthorizedHandler
    ): Result<T, DataError> = request(HttpMethod.Get, path, EmptyBody, params, true, onUnauthorized)

    suspend inline fun <reified T, reified Body> post(
        path: String,
        body: Body,
        noinline onUnauthorized: () -> Unit = defaultUnauthorizedHandler
    ): Result<T, DataError> = request(HttpMethod.Post, path, body, {}, false, onUnauthorized)

    suspend inline fun <reified T, reified Body> postAuth(
        path: String,
        body: Body,
        noinline onUnauthorized: () -> Unit = defaultUnauthorizedHandler
    ): Result<T, DataError> = request(HttpMethod.Post, path, body, {}, true, onUnauthorized)

    suspend inline fun <reified T, reified Body> request(
        method: HttpMethod,
        path: String,
        body: Body? = null,
        crossinline params: ParametersBuilder.() -> Unit = {},
        withAuth: Boolean = false,
        noinline onUnauthorized: () -> Unit = defaultUnauthorizedHandler
    ): Result<T, DataError> {
        return try {
            val result: T = `access$httpClient`.request("$baseUrl/$path") {
                this.method = method

                if (withAuth) {
                    //TODO: replace with real token get
//                    val token = localStorage.getAccessToken()
                    header(HttpHeaders.Authorization, "Bearer token")
                }

                if (body != null) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }

                url {
                    parameters.apply(params)
                }
            }.body()

            Result.Success(result)
        } catch (e: RedirectResponseException) {
            Result.Error(DataError.Network.UNKNOWN)
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                //TODO: make refresh request and if it's failed than call onUnauthorized
                onUnauthorized()
            }

            val error = when (e.response.status) {
                HttpStatusCode.RequestTimeout -> DataError.Network.REQUEST_TIMEOUT
                HttpStatusCode.TooManyRequests -> DataError.Network.TOO_MANY_REQUESTS
                HttpStatusCode.PayloadTooLarge -> DataError.Network.PAYLOAD_TOO_LARGE
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
}

@Serializable
data object EmptyBody
