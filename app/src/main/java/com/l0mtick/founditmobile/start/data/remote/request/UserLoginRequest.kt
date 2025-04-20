package com.l0mtick.founditmobile.start.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequest(
    val username: String,
    val password: String
)