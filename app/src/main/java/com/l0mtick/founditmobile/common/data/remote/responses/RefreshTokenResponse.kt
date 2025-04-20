package com.l0mtick.founditmobile.common.data.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)
