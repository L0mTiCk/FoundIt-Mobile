package com.l0mtick.founditmobile.main.presentation.search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarManager
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarType
import com.l0mtick.founditmobile.common.domain.error.LocationError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.common.presentation.util.UiText
import com.l0mtick.founditmobile.main.domain.model.LostItem
import com.l0mtick.founditmobile.main.domain.model.PaginatedData
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
    private val locationService: LocationService,
    private val snackbarManager: SnackbarManager,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val route = savedStateHandle.toRoute<NavigationRoute.Main.Search>()

    private val searchRadius = 500.0

    private val _state = MutableStateFlow<SearchState>(
        SearchState.ListScreen(
            isLoading = true,
            loadingStep = LoadingStep.FETCHING_CATEGORIES
        )
    )
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val eventChannel = Channel<SearchEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadInitialItems()
    }

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnCategorySelect -> selectCategory(action.id)
            is SearchAction.OnModeChange -> changeMode()
            SearchAction.OnCenterOnUser -> centerOnUserLocation()
            is SearchAction.OnListSearchValueChange -> updateListScreenSearch(action.value)
            is SearchAction.OnDateSelected -> updateSelectedDate(action.timestamp)
            SearchAction.OnDateCleared -> clearSelectedDate()
            SearchAction.OnLoadMoreListItems -> loadMoreListItems()
            SearchAction.OnRetry -> performSearch()
            SearchAction.OnRemoveError -> removeError()
            SearchAction.OnPerformSearch -> performSearch()
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
        performSearch()
    }

    private fun changeMode() {
        val current = _state.value
        when (current) {
            is SearchState.ListScreen -> {
                _state.update {
                    SearchState.MapScreen()
                }
                loadMapItems()
            }

            is SearchState.MapScreen -> {
                _state.update {
                    SearchState.ListScreen()
                }
                loadInitialItems()
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

    private fun updateListScreenSearch(value: String) {
        val current = state.value
        if (current !is SearchState.ListScreen) return

        _state.update {
            current.copy(
                searchValue = value
            )
        }
    }

    private fun updateSelectedDate(timestamp: Long) {
        val current = state.value
        if (current !is SearchState.ListScreen) return

        _state.update {
            current.copy(
                selectedDate = timestamp
            )
        }
        performSearch()
    }

    private fun clearSelectedDate() {
        val current = state.value
        if (current !is SearchState.ListScreen) return

        _state.update {
            current.copy(
                selectedDate = null
            )
        }
        performSearch()
    }

    private fun loadMoreListItems() {
        val current = state.value
        if (current !is SearchState.ListScreen) return

        viewModelScope.launch {
            if (current.items.hasMore) {
                _state.update {
                    current.copy(
                        isLoadingMore = true
                    )
                }
                val result = lostItemRepository.searchLostItems(
                    searchQuery = current.searchValue,
                    categoryIds = current.categories.map { it.id.toInt() },
                    userLatitude = current.userLocation.latitude,
                    userLongitude = current.userLocation.longitude,
                    radiusKm = searchRadius,
                    afterId = current.items.nextCursor,
                    date = current.selectedDate,
                    limit = null
                )
                when (result) {
                    is Result.Success -> {
                        val newPaginatedData = PaginatedData<LostItem>(
                            items = current.items.items + result.data.items,
                            hasMore = result.data.hasMore,
                            nextCursor = result.data.nextCursor
                        )
                        _state.update {
                            current.copy(
                                items = newPaginatedData,
                                isLoadingMore = false
                            )
                        }
                    }

                    is Result.Error -> {
                        _state.update {
                            current.copy(
                                isLoadingMore = false,
                                error = UiText.StringResource(R.string.loading_items_error)
                            )
                        }
                    }
                }
            } else {
                snackbarManager.showSnackbar(
                    UiText.StringResource(R.string.loading_next_empty_error),
                    SnackbarType.INFO
                )
            }
        }
    }

    private fun loadInitialItems() {
        viewModelScope.launch {
            // Start with loading state
            _state.update { currentState ->
                when (currentState) {
                    is SearchState.ListScreen -> currentState.copy(
                        isLoading = true,
                        loadingStep = LoadingStep.FETCHING_CATEGORIES,
                        error = null
                    )

                    is SearchState.MapScreen -> currentState.copy(
                        isLoading = true,
                        loadingStep = LoadingStep.FETCHING_CATEGORIES,
                        error = null
                    )
                }
            }

            val categoriesDeferred = async { categoriesRepository.getAllCategories() }

            // Update loading step to fetching location
            _state.update { currentState ->
                when (currentState) {
                    is SearchState.ListScreen -> currentState.copy(loadingStep = LoadingStep.FETCHING_LOCATION)
                    is SearchState.MapScreen -> currentState.copy(loadingStep = LoadingStep.FETCHING_LOCATION)
                }
            }

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
                    // Show error in current screen
                    _state.update { currentState ->
                        when (currentState) {
                            is SearchState.ListScreen -> currentState.copy(
                                isLoading = false,
                                error = UiText.StringResource(R.string.loading_items_location_error)
                            )

                            is SearchState.MapScreen -> currentState.copy(
                                isLoading = false,
                                error = UiText.StringResource(R.string.loading_items_location_error)
                            )
                        }
                    }
                    return@launch
                }

                is Result.Success -> {
                    val location = locationResult.data

                    // Update loading step to fetching data
                    _state.update { currentState ->
                        when (currentState) {
                            is SearchState.ListScreen -> currentState.copy(loadingStep = LoadingStep.FETCHING_DATA)
                            is SearchState.MapScreen -> currentState.copy(loadingStep = LoadingStep.FETCHING_DATA)
                        }
                    }

                    val categoriesResult = categoriesDeferred.await()

                    when (categoriesResult) {
                        is Result.Error -> {
                            // Show error in current screen
                            _state.update { currentState ->
                                when (currentState) {
                                    is SearchState.ListScreen -> currentState.copy(
                                        isLoading = false,
                                        error = UiText.StringResource(R.string.loading_items_categories_error)
                                    )

                                    is SearchState.MapScreen -> currentState.copy(
                                        isLoading = false,
                                        error = UiText.StringResource(R.string.loading_items_categories_error)
                                    )
                                }
                            }
                            return@launch
                        }

                        is Result.Success -> {
                            val categories = categoriesResult.data

                            val itemsResult = lostItemRepository.searchLostItems(
                                searchQuery = null,
                                categoryIds = route.categoryIds?.map { it.toInt() },
                                userLatitude = location.latitude,
                                userLongitude = location.longitude,
                                radiusKm = searchRadius,
                                afterId = null,
                                limit = null,
                                date = null
                            )

                            when (itemsResult) {
                                is Result.Error -> {
                                    // Show error in current screen
                                    _state.update { currentState ->
                                        when (currentState) {
                                            is SearchState.ListScreen -> currentState.copy(
                                                isLoading = false,
                                                error = UiText.StringResource(R.string.loading_items_error)
                                            )

                                            is SearchState.MapScreen -> currentState.copy(
                                                isLoading = false,
                                                error = UiText.StringResource(R.string.loading_items_error)
                                            )
                                        }
                                    }
                                }

                                is Result.Success -> {
                                    val selectedCategories = route.categoryIds ?: emptySet()
                                    // Update with successful data and remove loading state
                                    _state.update { currentState ->
                                        when (currentState) {
                                            is SearchState.ListScreen -> currentState.copy(
                                                userLocation = location,
                                                categories = categories,
                                                items = itemsResult.data,
                                                selectedCategories = selectedCategories.toSet(),
                                                isLoading = false,
                                                loadingStep = null,
                                                error = null
                                            )

                                            is SearchState.MapScreen -> SearchState.ListScreen(
                                                userLocation = location,
                                                categories = categories,
                                                items = itemsResult.data,
                                                selectedCategories = selectedCategories.toSet(),
                                                isLoading = false,
                                                loadingStep = null,
                                                error = null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadMapItems() {
        val current = _state.value
        if (current !is SearchState.MapScreen) return

        viewModelScope.launch {
            _state.update {
                current.copy(
                    isLoading = true,
                    loadingStep = LoadingStep.FETCHING_LOCATION
                )
            }
            val location = if (locationService.locationAvailabilityState.value.isLocationAvailable) {
                locationService.getCurrentLocation()
            } else {
                Result.Error(LocationError.LOCATION_UNAVAILABLE)
            }
            when(location) {
                is Result.Error -> {
                    _state.update {
                        current.copy(
                            isLoading = false,
                            error = UiText.StringResource(R.string.loading_items_location_error)
                        )
                    }
                }
                is Result.Success -> {
                    _state.update {
                        current.copy(
                            isLoading = true,
                            loadingStep = LoadingStep.FETCHING_DATA
                        )
                    }
                    val items = lostItemRepository.getItemsForMap(
                        userLatitude = location.data.latitude,
                        userLongitude = location.data.longitude,
                        radiusKm = searchRadius
                    )
                    when(items) {
                        is Result.Success -> {
                            _state.update {
                                current.copy(
                                    isLoading = false,
                                    error = null,
                                    items = items.data,
                                    loadingStep = null
                                )
                            }
                        }
                        is Result.Error -> {
                            _state.update {
                                current.copy(
                                    isLoading = false,
                                    error = UiText.StringResource(R.string.loading_items_location_error)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun performSearch() {
        viewModelScope.launch {
            _state.update { currentState ->
                when (currentState) {
                    is SearchState.ListScreen -> {
                        currentState.copy(
                            isLoading = true,
                            loadingStep = LoadingStep.FETCHING_DATA
                        )
                    }

                    is SearchState.MapScreen -> {
                        currentState.copy(
                            isLoading = true,
                            loadingStep = LoadingStep.FETCHING_DATA
                        )
                    }
                }
            }
            val currentState = _state.value
            when (currentState) {
                is SearchState.ListScreen -> {
                    Log.d(
                        "search_query",
                        "Text: ${currentState.searchValue}\nIds: ${currentState.selectedCategories}\nDate: ${currentState.selectedDate}"
                    )
                    val newItems = lostItemRepository.searchLostItems(
                        searchQuery = currentState.searchValue,
                        categoryIds = currentState.selectedCategories.map { it.toInt() },
                        userLatitude = currentState.userLocation.latitude,
                        userLongitude = currentState.userLocation.longitude,
                        radiusKm = searchRadius,
                        afterId = null,
                        limit = null,
                        date = currentState.selectedDate
                    )
                    when (newItems) {
                        is Result.Success -> {
                            _state.update {
                                currentState.copy(
                                    items = newItems.data,
                                    isLoading = false
                                )
                            }
                        }

                        is Result.Error -> {
                            _state.update {
                                currentState.copy(
                                    isLoading = false,
                                    error = UiText.StringResource(R.string.loading_items_error)
                                )
                            }
                            Log.e("search_viewmodel", "Error while searching: ${newItems.error}")
                        }
                    }
                }
                else -> {}
            }

        }
    }

    private fun removeError() {
        _state.update { currentState ->
            when (currentState) {
                is SearchState.ListScreen -> currentState.copy(
                    error = null
                )

                is SearchState.MapScreen -> currentState.copy(
                    error = null
                )
            }
        }
    }
}