package com.l0mtick.founditmobile.main.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDTO(
    val id: Long,
    val nameRu: String = "",
    val nameEn: String = "",
    val imageUrl: String = ""
)