package com.l0mtick.founditmobile.main.presentation

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.common.domain.error.LocationError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.domain.repository.LocationService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface LocationUiEvent {
    data object RequestPermissions : LocationUiEvent
    data object RequestGpsEnable : LocationUiEvent
    data class ShowError(val error: LocationError) : LocationUiEvent // More specific error message maybe
}

class MainScreenViewModel(
    private val locationService: LocationService
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()

    // Channel for one-off events like triggering launchers
    private val _eventChannel = Channel<LocationUiEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    init {
        // Collect the availability state from the service
        viewModelScope.launch {
            locationService.locationAvailabilityState.collect { availability ->
                _uiState.update {
                    it.copy(
                        locationAvailability = availability,
                        // Show the dialog if location is fundamentally unavailable
                        showLocationDialog = !availability.isLocationAvailable
                    )
                }
                // If availability changes, clear previous errors
                if (availability.isLocationAvailable && _uiState.value.error != null) {
                    _uiState.update { it.copy(error = null) }
                }
            }
        }
        // Initial check or fetch if needed
        // checkAndFetchLocation() // Example: fetch on init if possible
    }

    fun requestLocationData() {
        val availability = _uiState.value.locationAvailability
        when {
            !availability.arePlayServicesAvailable -> {
                viewModelScope.launch { _eventChannel.send(LocationUiEvent.ShowError(LocationError.PLAY_SERVICES_UNAVAILABLE)) }
                // Optionally trigger Play Services update flow if possible/needed
            }
            !availability.hasLocationPermission -> {
                // Trigger permission request via UI event
                viewModelScope.launch { _eventChannel.send(LocationUiEvent.RequestPermissions) }
            }
            !availability.isGpsEnabled -> {
                // Trigger GPS enable request via UI event
                viewModelScope.launch { _eventChannel.send(LocationUiEvent.RequestGpsEnable) }
            }
            else -> {
                // Everything looks okay, attempt to fetch
                fetchCurrentLocation()
            }
        }
    }


    private fun fetchCurrentLocation() {
        if (_uiState.value.isLoadingLocation) return // Prevent concurrent requests

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingLocation = true, error = null) }
            when (val result = locationService.getCurrentLocation()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(isLoadingLocation = false, currentLocation = result.data)
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoadingLocation = false, error = result.error)
                    }
                    // Handle specific errors if needed (e.g., show specific messages)
                    viewModelScope.launch { _eventChannel.send(LocationUiEvent.ShowError(result.error)) }
                }
            }
        }
    }

    // Called from UI after permission result
    fun onPermissionResult(grantedPermissions: Map<String, Boolean>) {
        locationService.updatePermissionStatus(grantedPermissions)
        // After updating status, re-evaluate if location can be requested now
        if (locationService.locationAvailabilityState.value.hasLocationPermission) {
            requestLocationData() // Try again now that permissions might be granted
        } else {
            // Handle case where permissions were still denied
            viewModelScope.launch { _eventChannel.send(LocationUiEvent.ShowError(LocationError.PERMISSION_DENIED)) }
            // Check for permanent denial if Activity reference is available via callback
            // viewModelScope.launch { _eventChannel.emit(LocationUiEvent.ShowError(LocationError.PERMISSION_DENIED_PERMANENTLY)) }
        }
    }

    // Called from UI after GPS settings activity result
    fun onGpsResult(activityResultOk: Boolean) {
        // Refresh GPS status regardless of result, as user might have manually changed it
        locationService.refreshGpsStatus()
        if (activityResultOk && locationService.locationAvailabilityState.value.isGpsEnabled) {
            // GPS was successfully enabled (or already was), try requesting location again
            requestLocationData()
        } else if (!locationService.locationAvailabilityState.value.isGpsEnabled){
            // GPS is still disabled
            viewModelScope.launch { _eventChannel.send(LocationUiEvent.ShowError(LocationError.GPS_DISABLED)) }
        }
    }

    // --- UI Trigger Wrappers ---
    // These functions are called by the UI to ask the VM to coordinate with the Service

    fun triggerPermissionRequest(launcher: ActivityResultLauncher<Array<String>>) {
        locationService.requestLocationPermission(launcher)
    }

    fun triggerGpsEnableRequest(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        viewModelScope.launch {
            val launched = locationService.requestGpsEnable(launcher)
            if (!launched && !locationService.locationAvailabilityState.value.isGpsEnabled) {
                // If the prompt wasn't launched (e.g., error checking settings) and GPS is still off
                _eventChannel.send(LocationUiEvent.ShowError(LocationError.GPS_DISABLED)) // Or a more specific error
            }
        }
    }
}