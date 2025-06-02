package com.l0mtick.founditmobile.main.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class LostItemStatus {
    PENDING,   // Ожидает модерации
    ACTIVE,    // Опубликована и видна всем
    EXPIRED,   // Истек срок публикации
    FOUND      // Найдена
}