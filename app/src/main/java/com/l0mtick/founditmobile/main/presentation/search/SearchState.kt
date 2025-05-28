package com.l0mtick.founditmobile.main.presentation.search

import android.location.Location
import androidx.annotation.StringRes
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.util.UiText
import com.l0mtick.founditmobile.main.domain.model.Category
import com.l0mtick.founditmobile.main.domain.model.LostItem
import com.l0mtick.founditmobile.main.domain.model.PaginatedData

sealed class SearchState {

    data class ListScreen(
        val searchValue: String = "",
        val userLocation: Location = Location(""),
        val categories: List<Category> = emptyList(),
        val selectedCategories: Set<Long> = emptySet(),
        val selectedDate: Long? = null,
        val items: PaginatedData<LostItem> = PaginatedData(emptyList(), false, null),
        val isLoadingMore: Boolean = false,
        val isLoading: Boolean = false,
        val loadingStep: LoadingStep? = null,
        val error: UiText? = null
    ) : SearchState()

    data class MapScreen(
        val items: List<LostItem> = emptyList(),
        val isLoading: Boolean = false,
        val loadingStep: LoadingStep? = null,
        val error: UiText? = null
    ) : SearchState()
}

enum class LoadingStep(@StringRes val userString: Int) {
    FETCHING_CATEGORIES(R.string.fetching_categories),
    FETCHING_LOCATION(R.string.fetching_location),
    FETCHING_DATA(R.string.fetching_items),
}