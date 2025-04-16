package com.l0mtick.founditmobile

import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.start.data.remote.api.AuthApiImpl
import com.l0mtick.founditmobile.start.domain.repository.AuthApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

class AuthApiTest {

    private lateinit var api: AuthApi

    @Before
    fun setUp() {
        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                json( Json { prettyPrint = true } )
            }
        }
        val fakeStorage = object : LocalStorage {
            override suspend fun setToken(token: String) = Unit

            override suspend fun getToken(): String? = null

            override suspend fun setRefreshToken(token: String) = Unit

            override suspend fun getRefreshToken(): String? = null

            override suspend fun setIsLoggedIn(isLoggedIn: Boolean) = Unit

            override suspend fun isLoggedIn(): Boolean = false

            override suspend fun setEmail(email: String) = Unit

            override suspend fun getEmail(): String? = null

            override suspend fun clear() = Unit
        }

        api = AuthApiImpl(client, fakeStorage)
    }

    @Test
    fun `login with valid login and password returns token`() = runBlocking {
        val response = api.loginByLogin("test3", "Password")
        assertTrue(response is Result.Success)
    }
}