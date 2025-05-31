package com.l0mtick.founditmobile.main.presentation.chat

sealed interface ChatEvent {
    data object NavigateToInbox : ChatEvent
}