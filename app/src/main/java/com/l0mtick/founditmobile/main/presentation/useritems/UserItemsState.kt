package com.l0mtick.founditmobile.main.presentation.useritems

import com.l0mtick.founditmobile.main.domain.model.LostItem

sealed class UserItemsState {
    data class UserFavoriteItemsState(
        val isLoading: Boolean = false,
        val items: List<LostItem> = emptyList()
    ) : UserItemsState()

    data class UserCreatedItemsState(
        val isLoading: Boolean = false,
        val items: List<LostItem> = emptyList()
    ) : UserItemsState()
}