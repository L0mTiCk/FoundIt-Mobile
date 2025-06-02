package com.l0mtick.founditmobile.main.domain.model

data class LostItem(
    val id: Int,
    val userId: Int,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val description: String?,
    val isFound: Boolean,
    val createdAt: Long,
    val expiresAt: Long,
    val photoUrls: List<String>,
    val isModerated: Boolean,
    val status: LostItemStatus,
    val categories: List<Category>
)