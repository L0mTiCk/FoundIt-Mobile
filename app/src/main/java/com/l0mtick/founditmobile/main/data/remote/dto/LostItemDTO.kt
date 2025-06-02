package com.l0mtick.founditmobile.main.data.remote.dto

import com.l0mtick.founditmobile.main.domain.model.LostItemStatus
import kotlinx.serialization.Serializable

@Serializable
data class LostItemDTO(
    val id: Int,
    val userId: Int,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val description: String?,
    val isFound: Boolean = false,
    val createdAt: Long,
    val expiresAt: Long,
    val photoUrls: List<String>,
    val isModerated: Boolean = false,
    val status: LostItemStatus,
    val categories: List<CategoryDTO>
)