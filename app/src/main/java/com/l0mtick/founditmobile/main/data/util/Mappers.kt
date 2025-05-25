package com.l0mtick.founditmobile.main.data.util

import com.l0mtick.founditmobile.common.data.remote.dto.UserDTO
import com.l0mtick.founditmobile.main.data.remote.dto.CategoryDTO
import com.l0mtick.founditmobile.main.data.remote.dto.ChatDTO
import com.l0mtick.founditmobile.main.data.remote.dto.LostItemDTO
import com.l0mtick.founditmobile.main.data.remote.responses.PaginatedResponse
import com.l0mtick.founditmobile.main.domain.model.Category
import com.l0mtick.founditmobile.main.domain.model.Chat
import com.l0mtick.founditmobile.main.domain.model.LostItem
import com.l0mtick.founditmobile.main.domain.model.PaginatedData
import com.l0mtick.founditmobile.main.domain.model.User
import java.util.Locale

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

fun ChatDTO.toModel(): Chat = Chat(
    id = id,
    interlocutor = interlocutor.toModel(),
    ownerItemTitle = ownerItemTitle,
    ownerItemImageUrl = ownerItemImageUrl,
    lastMessage = lastMessage,
    lastMessageAt = lastMessageAt
)

fun LostItemDTO.toModel(): LostItem {
    return LostItem(
        id = this.id,
        userId = this.userId,
        latitude = this.latitude,
        longitude = this.longitude,
        title = this.title,
        description = this.description,
        isFound = this.isFound,
        createdAt = this.createdAt,
        expiresAt = this.expiresAt,
        photoUrls = this.photoUrls,
        isModerated = this.isModerated,
        status = this.status,
        categories = this.categories.map { it.toModel(Locale.getDefault().language) }
    )
}

fun <T_DTO, T_Model> PaginatedResponse<T_DTO>.toModel(
    itemMapper: (T_DTO) -> T_Model
): PaginatedData<T_Model> {
    return PaginatedData(
        items = this.items.map(itemMapper),
        hasMore = this.hasMore,
        nextCursor = this.nextCursor
    )
}
