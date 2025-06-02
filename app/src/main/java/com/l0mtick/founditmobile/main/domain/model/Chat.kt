package com.l0mtick.founditmobile.main.domain.model

data class Chat(
    val id: Int,
    val interlocutor: User,
    val ownerItemTitle: String,
    val ownerItemImageUrl: String,
    val lastMessage: String?,
    val lastMessageAt: Long?
)
