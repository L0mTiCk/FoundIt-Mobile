package com.l0mtick.founditmobile.main.data.remote.responses

import com.l0mtick.founditmobile.main.data.remote.dto.CategoryDTO
import kotlinx.serialization.Serializable

@Serializable
data class CategoriesResponse(
    val categories: List<CategoryDTO> = emptyList()
)
