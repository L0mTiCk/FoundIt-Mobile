package com.l0mtick.founditmobile.main.presentation.search

sealed class SearchAction {
    data class OnCategorySelect(val id: Long) : SearchAction()
    object OnModeChange : SearchAction()
    object OnCenterOnUser : SearchAction()
    data class OnListSearchValueChange(val value: String) : SearchAction()
    data class OnDateSelected(val timestamp: Long) : SearchAction()
    object OnDateCleared : SearchAction()
    object OnLoadMoreListItems : SearchAction()
    object OnRetry : SearchAction()
    object OnRemoveError : SearchAction()
    object OnPerformSearch : SearchAction()
}