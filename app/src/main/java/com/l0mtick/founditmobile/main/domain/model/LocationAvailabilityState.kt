package com.l0mtick.founditmobile.main.domain.model

// Data class to hold the detailed state of location availability
data class LocationAvailabilityState(
    val hasFinePermission: Boolean = false,
    val hasCoarsePermission: Boolean = false,
    val isGpsEnabled: Boolean = false,
    val arePlayServicesAvailable: Boolean = true // Assume available initially
) {
    // Computed property to easily check if location can be requested
    val hasLocationPermission: Boolean
        get() = hasFinePermission || hasCoarsePermission

    // Computed property to check if everything is ready
    val isLocationAvailable: Boolean
        get() = hasLocationPermission && isGpsEnabled && arePlayServicesAvailable
}