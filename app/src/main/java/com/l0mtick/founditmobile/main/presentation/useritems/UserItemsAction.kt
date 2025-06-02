package com.l0mtick.founditmobile.main.presentation.useritems

sealed interface UserItemsAction {
    data class DeleteItem(val itemId: Int) : UserItemsAction
    data class RemoveFromFavorites(val itemId: Int) : UserItemsAction
    data class MarkAsReturned(val itemId: Int) : UserItemsAction
}