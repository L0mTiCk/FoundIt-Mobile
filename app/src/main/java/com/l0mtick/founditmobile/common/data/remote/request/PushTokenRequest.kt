package com.l0mtick.founditmobile.common.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class PushTokenRequest(
    val pushToken: String
)