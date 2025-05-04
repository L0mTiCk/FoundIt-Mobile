package com.l0mtick.founditmobile.main.presentation.inbox

import com.l0mtick.founditmobile.main.domain.model.Chat

data class InboxState(
    val isLoading: Boolean = true,
    val chats: List<Chat>? = null,
)