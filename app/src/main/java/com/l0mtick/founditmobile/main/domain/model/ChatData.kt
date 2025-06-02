package com.l0mtick.founditmobile.main.domain.model

data class ChatData(
    val itemTitle: String,
    val itemDescription: String,
    val itemPictureUrl: String,
    val interlocutor: User,
    val messages: List<Message>
)
