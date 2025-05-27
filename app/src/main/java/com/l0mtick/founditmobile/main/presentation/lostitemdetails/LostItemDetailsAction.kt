package com.l0mtick.founditmobile.main.presentation.lostitemdetails

sealed interface LostItemDetailsAction {
    data object OnCreateChatForItem : LostItemDetailsAction
    data object OnAddToFavorites : LostItemDetailsAction
}