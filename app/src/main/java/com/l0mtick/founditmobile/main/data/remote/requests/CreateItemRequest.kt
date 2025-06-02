package com.l0mtick.founditmobile.main.data.remote.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateItemRequest(
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val description: String,
    val expiresAt: Long,
    val photoUrls: List<String>,
    val categoryIds: List<Int>
)