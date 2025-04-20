package com.l0mtick.founditmobile.start.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class PhoneVerifyRequest(
    val phoneNumber: String,
    val code: String
)