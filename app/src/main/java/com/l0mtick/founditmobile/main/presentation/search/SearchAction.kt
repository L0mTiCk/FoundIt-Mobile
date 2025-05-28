package com.l0mtick.founditmobile.main.presentation.search

sealed interface SearchAction {
    data class OnCategorySelect(val id: Long) : SearchAction
    data object OnModeChange : SearchAction
    data object OnCenterOnUser : SearchAction
    data class OnListSearchValueChange(val value: String) : SearchAction
    data class OnDateSelected(val timestamp: Long) : SearchAction
    data object OnDateCleared : SearchAction
    data object OnLoadMoreListItems : SearchAction
}