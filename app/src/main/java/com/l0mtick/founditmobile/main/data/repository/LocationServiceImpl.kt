package com.l0mtick.founditmobile.main.data.repository

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.CancellationTokenSource
import com.l0mtick.founditmobile.common.domain.error.LocationError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.domain.model.LocationAvailabilityState
import com.l0mtick.founditmobile.main.domain.repository.LocationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LocationServiceImpl(
    private val application: Application // Use Application context to avoid leaks
) : LocationService {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private val settingsClient: SettingsClient = LocationServices.getSettingsClient(application)
    private val locationManager: LocationManager =
        application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // Scope for launching coroutines within the service lifecycle
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _locationAvailabilityState = MutableStateFlow(LocationAvailabilityState())
    override val locationAvailabilityState: StateFlow<LocationAvailabilityState> =
        _locationAvailabilityState.asStateFlow()

    // Define required permissions (Fine + Coarse as recommended)
    override val requiredPermissions: List<String> = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    init {
        // Perform initial checks when the service is created
        checkPlayServices()
        checkInitialPermissions()
        refreshGpsStatus() // Initial GPS check
    }

    override fun checkPlayServices() {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(application)
        val isAvailable = resultCode == ConnectionResult.SUCCESS
        _locationAvailabilityState.update { it.copy(arePlayServicesAvailable = isAvailable) }
        if (!isAvailable) {
            // Optionally handle cases where Play Services can be made available (e.g., user updates)
            // For now, just update the state. The UI should block based on isLocationAvailable.
            println("LocationService: Google Play Services unavailable (code: $resultCode)")
        }
    }

    private fun checkInitialPermissions() {
        val hasFine = ContextCompat.checkSelfPermission(
            application, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(
            application, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        _locationAvailabilityState.update {
            it.copy(hasFinePermission = hasFine, hasCoarsePermission = hasCoarse)
        }
    }

    @SuppressLint("MissingPermission") // Permissions checked before calling FusedLocationProviderClient methods
    override suspend fun getCurrentLocation(): LocationResult {
        val currentState = locationAvailabilityState.value // Get current state

        // Pre-checks
        if (!currentState.arePlayServicesAvailable) {
            return Result.Error(LocationError.PLAY_SERVICES_UNAVAILABLE)
        }
        if (!currentState.hasLocationPermission) {
            return Result.Error(LocationError.PERMISSION_DENIED)
        }
        if (!currentState.isGpsEnabled) {
            return Result.Error(LocationError.GPS_DISABLED)
        }

        // Use Cancellation Token Source for better cancellation control
        val cancellationTokenSource = CancellationTokenSource()

        return try {
            // Request current location with high accuracy
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, // Or PRIORITY_BALANCED_POWER_ACCURACY if coarse is acceptable
                cancellationTokenSource.token
            ).await() // Using kotlinx-coroutines-play-services await() extension

            if (location != null) {
                Result.Success<Location, LocationError>(location)
            } else {
                // Fallback or error if location is null even after successful task
                Result.Error(LocationError.LOCATION_UNAVAILABLE)
            }
        } catch (e: SecurityException) {
            // Although checked, catch potential SecurityException
            println("LocationService Error: SecurityException during getCurrentLocation. $e")
            // Re-check permissions as state might be inconsistent
            checkInitialPermissions()
            Result.Error(LocationError.PERMISSION_DENIED)
        } catch (e: Exception) {
            // Catch other potential exceptions from the await() call or client
            println("LocationService Error: Exception during getCurrentLocation. $e")
            Result.Error(LocationError.UNKNOWN)
        } finally {
            // Cancel the token source if the coroutine is cancelled externally
            // Although getCurrentLocation typically completes quickly, it's good practice.
            cancellationTokenSource.cancel()
        }
    }


    override fun requestLocationPermission(launcher: ActivityResultLauncher<Array<String>>) {
        // The actual launching is done by the UI component owning the launcher
        launcher.launch(requiredPermissions.toTypedArray())
    }

    override suspend fun requestGpsEnable(launcher: ActivityResultLauncher<IntentSenderRequest>): Boolean {
        if (locationAvailabilityState.value.isGpsEnabled) {
            return false // Already enabled, no need to prompt
        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000L) // Dummy request
            .setMinUpdateIntervalMillis(5000L)
            .build()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        builder.setAlwaysShow(true) // Ensure dialog is always shown if needed

        return try {
            withContext(Dispatchers.Main) { // SettingsClient needs to be called from Main thread usually
                settingsClient.checkLocationSettings(locationSettingsRequest).await()
                // Settings are adequate, GPS might have been enabled just now or was already on
                // (though we checked `isGpsEnabled` earlier). Refresh status.
                refreshGpsStatus()
                false // Indicate prompt wasn't needed or settings are now ok
            }
        } catch (e: Exception) {
            // Handle exception, specifically ResolvableApiException
            if (e is com.google.android.gms.common.api.ResolvableApiException) {
                try {
                    // Show the dialog by calling startResolutionForResult()
                    val intentSenderRequest = IntentSenderRequest.Builder(e.resolution).build()
                    withContext(Dispatchers.Main) { // Launching activity must be on Main thread
                        launcher.launch(intentSenderRequest)
                    }
                    true // Indicate prompt was launched
                } catch (sendEx: Exception) {
                    // Log or handle the error sending the intent
                    println("LocationService Error: Could not launch GPS enable intent. $sendEx")
                    false
                }
            } else {
                // Other exceptions during checkLocationSettings
                println("LocationService Error: Could not check location settings. $e")
                refreshGpsStatus() // Refresh status in case of other errors
                false
            }
        }
    }


    override fun updatePermissionStatus(grantedPermissions: Map<String, Boolean>) {
        val hasFine = grantedPermissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val hasCoarse = grantedPermissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        _locationAvailabilityState.update {
            it.copy(hasFinePermission = hasFine, hasCoarsePermission = hasCoarse)
        }
        // Optional: Check if denied permanently (requires Activity reference, harder in service)
        // You might need to handle PERMISSION_DENIED_PERMANENTLY in the UI layer based on
        // shouldShowRequestPermissionRationale after a denial.
    }

    override fun refreshGpsStatus() {
        val isGpsEnabled = try {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            println("LocationService Error: Could not check GPS status. $e")
            false // Assume disabled if check fails
        }
        _locationAvailabilityState.update { it.copy(isGpsEnabled = isGpsEnabled) }
    }
}

typealias LocationResult = Result<Location, LocationError>