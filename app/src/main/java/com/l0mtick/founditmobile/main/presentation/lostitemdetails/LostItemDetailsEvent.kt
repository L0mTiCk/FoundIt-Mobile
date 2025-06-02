package com.l0mtick.founditmobile.main.presentation.lostitemdetails

sealed interface LostItemDetailsEvent {
    data class NavigateToChat(val chatId: Int) : LostItemDetailsEvent
}