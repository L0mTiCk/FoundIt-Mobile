package com.l0mtick.founditmobile.main.data.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResponse<T>(
    val items: List<T>,
    val hasMore: Boolean,
    val nextCursor: Int?
)