package com.l0mtick.founditmobile.main.data.remote.responses

import com.l0mtick.founditmobile.main.data.remote.dto.ChatDTO
import kotlinx.serialization.Serializable

@Serializable
data class ChatsResponse(
    val chats: List<ChatDTO>?,
    val updateTime: Long
)
