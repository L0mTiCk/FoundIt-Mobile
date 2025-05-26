package com.l0mtick.founditmobile.main.domain.model

data class Message(
    val id: Int,
    val chatId: Int,
    val senderId: Int,
    val content: String,
    val createdAt: Long,
    val isRead: Boolean
)