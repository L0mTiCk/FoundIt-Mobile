package com.l0mtick.founditmobile.main.data.util

import com.l0mtick.founditmobile.main.data.remote.dto.CategoryDTO
import com.l0mtick.founditmobile.main.data.remote.dto.UserDTO
import com.l0mtick.founditmobile.main.domain.model.Category
import com.l0mtick.founditmobile.main.domain.model.User

fun CategoryDTO.toModel(languageCode: String = "en"): Category = Category(
    id = id,
    name = when(languageCode) {
        "ru" -> nameRu
        else -> nameEn
    },
    pictureUrl = imageUrl
)

fun UserDTO.toModel(): User = User(
    id = id,
    username = username,
    email = email,
    profilePictureUrl = logoUrl,
    level = level,
    levelItemsCount = itemCount,
    createdAt = createdAt
)