package com.l0mtick.founditmobile.main.data.remote.dto

import com.l0mtick.founditmobile.common.data.remote.dto.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class ChatDTO(
    val id: Int,
    val interlocutor: UserDTO,
    val ownerItemTitle: String,
    val ownerItemImageUrl: String,
    val lastMessage: String?,
    val lastMessageAt: Long?
)
