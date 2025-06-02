package com.l0mtick.founditmobile.common.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)

