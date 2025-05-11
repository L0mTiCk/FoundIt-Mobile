package com.l0mtick.founditmobile.main.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.common.domain.error.LocationError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.domain.repository.CategoriesRepository
import com.l0mtick.founditmobile.main.domain.repository.LocationService
import com.l0mtick.founditmobile.main.domain.repository.LostItemRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val categoriesRepository: CategoriesRepository,
    private val lostItemRepository: LostItemRepository,
    private val locationService: LocationService
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.ListScreen())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val eventChannel = Channel<SearchEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        init()
    }

    private fun init() {
        viewModelScope.launch {
            _state.value = SearchState.Loading(LoadingStep.FETCHING_CATEGORIES)
            val categoriesDeferred = async { categoriesRepository.getAllCategories() }

            _state.value = SearchState.Loading(LoadingStep.FETCHING_LOCATION)
            val locationDeferred = async {
                if (locationService.locationAvailabilityState.value.isLocationAvailable) {
                    locationService.getCurrentLocation()
                } else {
                    Result.Error(LocationError.LOCATION_UNAVAILABLE)
                }
            }

            val locationResult = locationDeferred.await()

            when (locationResult) {
                is Result.Error -> {
                    _state.value =
                        SearchState.Error("Failed to get location: ${locationResult.error}")
                    return@launch
                }

                is Result.Success -> {
                    val location = locationResult.data

                    _state.value = SearchState.Loading(LoadingStep.FETCHING_DATA)
                    val categoriesResult = categoriesDeferred.await()

                    when (categoriesResult) {
                        is Result.Error -> {
                            _state.value =
                                SearchState.Error("Failed to fetch categories: ${categoriesResult.error}")
                            return@launch
                        }

                        is Result.Success -> {
                            val categories = categoriesResult.data

                            val itemsResult = lostItemRepository.searchLostItems(
                                searchQuery = null,
                                categoryIds = null,
                                userLatitude = location.latitude,
                                userLongitude = location.longitude,
                                radiusKm = 5000.0,
                                afterId = null,
                                limit = null
                            )

                            when (itemsResult) {
                                is Result.Error -> {
                                    _state.value =
                                        SearchState.Error("Failed to fetch items: ${itemsResult.error}")
                                }

                                is Result.Success -> {
                                    Log.d("search_viewmodel", itemsResult.toString())
                                    _state.value = SearchState.ListScreen(
                                        categories = categories,
                                        items = itemsResult.data
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnCategorySelect -> selectCategory(action.id)
            is SearchAction.OnModeChange -> changeMode()
            SearchAction.OnCenterOnUser -> centerOnUserLocation()
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

    private fun changeMode() {
        val current = _state.value
        _state.update {
            when (current) {
                is SearchState.ListScreen -> {
                    SearchState.MapScreen(
                        items = current.items
                    )
                }

                is SearchState.MapScreen -> {
                    SearchState.ListScreen(
                        items = current.items
                    )
                }

                else -> {
                    SearchState.ListScreen()
                }
            }
        }
    }

    private fun centerOnUserLocation() {
        val current = _state.value
        if (current !is SearchState.MapScreen) return

        viewModelScope.launch {
            if (locationService.locationAvailabilityState.value.isLocationAvailable) {
                val location = locationService.getCurrentLocation()
                if (location is Result.Success) {
                    eventChannel.send(SearchEvent.MapLayoutEvent.CenterOnUserLocation(location.data))
                }
            }
        }
    }

}