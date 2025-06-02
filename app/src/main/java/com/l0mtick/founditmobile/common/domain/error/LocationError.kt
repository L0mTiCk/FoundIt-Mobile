package com.l0mtick.founditmobile.common.domain.error

enum class LocationError: Error  {
    PERMISSION_DENIED, // User denied necessary permissions
    PERMISSION_DENIED_PERMANENTLY, // User denied and checked "Don't ask again" (requires manual settings change)
    GPS_DISABLED, // Location services are turned off in system settings
    LOCATION_UNAVAILABLE, // FusedLocationProviderClient couldn't get a location
    PLAY_SERVICES_UNAVAILABLE, // Google Play Services are missing or outdated
    UNKNOWN // Generic error
}