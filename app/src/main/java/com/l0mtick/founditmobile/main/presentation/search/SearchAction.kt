package com.l0mtick.founditmobile.main.presentation.search

sealed interface SearchAction {
    data class OnCategorySelect(val id: Long) : SearchAction
}