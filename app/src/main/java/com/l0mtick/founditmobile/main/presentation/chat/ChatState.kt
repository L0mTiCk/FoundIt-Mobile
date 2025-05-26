package com.l0mtick.founditmobile.main.presentation.chat

import com.l0mtick.founditmobile.main.domain.model.Message
import com.l0mtick.founditmobile.main.domain.model.User

data class ChatState(
    val chatId: Int = -1,
    val itemTitle: String = "",
    val itemDescription: String = "",
    val itemPictureUrl: String = "",
    val interlocutor: User? = null,
    val messages: List<Message> = emptyList(),
    val messageInput: String = "",
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val isConnected: Boolean = false // Добавлено поле для отслеживания состояния WebSocket
)