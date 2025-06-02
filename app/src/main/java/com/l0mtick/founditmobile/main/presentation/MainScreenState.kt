package com.l0mtick.founditmobile.main.presentation

import android.location.Location
import com.l0mtick.founditmobile.common.domain.error.LocationError
import com.l0mtick.founditmobile.main.domain.model.LocationAvailabilityState

data class MainScreenState(
    val locationAvailability: LocationAvailabilityState = LocationAvailabilityState(),
    val currentLocation: Location? = null,
    val isLoadingLocation: Boolean = false,
    val error: LocationError? = null, // For showing specific errors
    val showLocationDialog: Boolean = false // Controls the blocking dialog
)
