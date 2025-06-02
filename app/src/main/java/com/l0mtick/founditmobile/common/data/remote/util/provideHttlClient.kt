package com.l0mtick.founditmobile.common.data.remote.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.time.Duration

fun provideHttpClient(): HttpClient = HttpClient(OkHttp) {
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
    
    install(WebSockets) {
        pingInterval = Duration.parse("20s")
    }

    expectSuccess = true

    defaultRequest {
        contentType(ContentType.Application.Json)
    }
}