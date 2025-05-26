package com.l0mtick.founditmobile.main.presentation.chat

sealed interface ChatAction {
    data object LoadMessages : ChatAction
    data object SendMessage : ChatAction
    data class UpdateMessageInput(val text: String) : ChatAction
}