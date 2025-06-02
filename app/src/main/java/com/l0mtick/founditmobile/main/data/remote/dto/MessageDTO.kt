package com.l0mtick.founditmobile.main.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageDTO(
    val id: Int,
    val chatId: Int,
    val senderId: Int,
    val content: String,
    val createdAt: Long,
    val isRead: Boolean
)