package com.l0mtick.founditmobile.main.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.main.domain.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class SearchViewModel : ViewModel() {

    private val categories = listOf(
        Category(
            1,
            "Wallets",
            ""
        ),
        Category(
            2,
            "Clothes",
            ""
        ),
        Category(
            3,
            "Electronics",
            ""
        ),
        Category(
            4,
            "Jewelry",
            ""
        ),
        Category(
            5,
            "Interpollationalism",
            ""
        ),
    )

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow<SearchState>(SearchState.ListScreen())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                _state.update {
                    SearchState.ListScreen(
                        categories = categories
                    )
                }
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SearchState.ListScreen()
        )

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnCategorySelect -> selectCategory(action.id)
        }
    }

    private fun selectCategory(id: Long) {
        val current = _state.value
        if (current !is SearchState.ListScreen) return

        _state.update { state ->
            val updated = if (id in current.selectedCategories)
                current.selectedCategories - id
            else
                current.selectedCategories + id

            current.copy(
                selectedCategories = updated
            )
        }
    }

}