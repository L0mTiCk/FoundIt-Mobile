package com.l0mtick.founditmobile.common.data.remote.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun provideHttpClient(): HttpClient = HttpClient(Android) {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            }
        )
    }

    install(HttpTimeout) {
        connectTimeoutMillis = 7000
        requestTimeoutMillis = 7000
    }

    expectSuccess = true

    defaultRequest {
        contentType(ContentType.Application.Json)
    }
}