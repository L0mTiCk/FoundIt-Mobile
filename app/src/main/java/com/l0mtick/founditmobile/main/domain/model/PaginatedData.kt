package com.l0mtick.founditmobile.main.domain.model

data class PaginatedData<T>(
    val items: List<T>,
    val hasMore: Boolean,
    val nextCursor: Int?
)