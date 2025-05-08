package com.l0mtick.founditmobile.main.presentation.search

import com.l0mtick.founditmobile.main.domain.model.Category
import com.l0mtick.founditmobile.main.domain.model.LostItem
import com.l0mtick.founditmobile.main.domain.model.PaginatedData

sealed class SearchState {

    data class Loading(val step: LoadingStep) : SearchState()

    data class Error(val message: String) : SearchState()

    data class ListScreen(
        val categories: List<Category> = emptyList(),
        val selectedCategories: Set<Long> = emptySet(),
        val items: PaginatedData<LostItem> = PaginatedData(emptyList(), false, null)
    ) : SearchState()

    data class MapScreen(
        val items: PaginatedData<LostItem> = PaginatedData(emptyList(), false, null)
    ) : SearchState()
}

enum class LoadingStep(val userString: String) {
    INITIALIZING("Initializing..."),
    FETCHING_CATEGORIES("Fetching categories..."),
    FETCHING_LOCATION("Getting your location..."),
    FETCHING_DATA("Fetching data..."),
    // You could add more detailed steps if needed
}