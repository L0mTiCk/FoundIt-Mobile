package com.l0mtick.founditmobile.main.presentation.search

import com.l0mtick.founditmobile.main.domain.model.Category

sealed class SearchState {
    data class ListScreen(
        val categories: List<Category> = emptyList(),
        val selectedCategories: Set<Long> = emptySet()
    ) : SearchState()

    data object MapScreen : SearchState()
}