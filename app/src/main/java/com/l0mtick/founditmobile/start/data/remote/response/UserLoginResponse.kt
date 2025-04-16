package com.l0mtick.founditmobile.start.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginResponse(
    val id: Int,
    val username: String,
    val email: String,
    val level: Int,
    val itemCount: Int,
    val createdAt: Long,
    val logoUrl: String?,
    val token: String,
    val refreshToken: String
)
