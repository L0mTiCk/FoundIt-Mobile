package com.l0mtick.founditmobile.main.data.util

import com.l0mtick.founditmobile.main.data.remote.dto.CategoryDTO
import com.l0mtick.founditmobile.main.domain.model.Category

fun CategoryDTO.toModel(languageCode: String = "en"): Category = Category(
    id = id,
    name = when(languageCode) {
        "ru" -> nameRu
        else -> nameEn
    },
    pictureUrl = imageUrl
)