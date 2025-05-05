package com.l0mtick.founditmobile.main.domain.repository

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.l0mtick.founditmobile.main.data.repository.LocationResult
import com.l0mtick.founditmobile.main.domain.model.LocationAvailabilityState
import kotlinx.coroutines.flow.StateFlow

interface LocationService {
    /**
     * A flow emitting the current state of location availability (permissions, GPS).
     * UI layers should collect this to react to changes.
     */
    val locationAvailabilityState: StateFlow<LocationAvailabilityState>

    /**
     * The list of location permissions required by the service.
     * Needed for the UI to request permissions.
     */
    val requiredPermissions: List<String>

    /**
     * Asynchronously attempts to fetch the current device location.
     * Checks for permissions and GPS status internally before fetching.
     *
     * @return [LocationResult.Success] with [Location] data if successful.
     * @return [LocationResult.Error] with a [LocationError] if fetching fails or prerequisites are not met.
     */
    suspend fun getCurrentLocation(): LocationResult

    /**
     * Initiates a request for location permissions using the provided launcher.
     * The caller (usually Composable/Fragment/Activity) must provide the launcher.
     *
     * @param launcher An [ActivityResultLauncher] registered for [androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions].
     */
    fun requestLocationPermission(launcher: ActivityResultLauncher<Array<String>>)

    /**
     * Checks if GPS is disabled and, if so, prepares an intent to prompt the user
     * to enable it via the provided launcher.
     *
     * @param launcher An [ActivityResultLauncher] registered for [androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult].
     * The service will check settings and call launch() on this launcher if needed.
     * @return true if the prompt was launched, false otherwise (e.g., GPS already enabled, error checking settings).
     */
    suspend fun requestGpsEnable(launcher: ActivityResultLauncher<IntentSenderRequest>): Boolean

    /**
     * Called by the UI layer after a permission request flow completes.
     * Updates the internal state based on the granted permissions.
     *
     * @param grantedPermissions A map of permission strings to boolean grant status, typically from the ActivityResultCallback.
     */
    fun updatePermissionStatus(grantedPermissions: Map<String, Boolean>)

    /**
     * Forces a re-check of the GPS status (e.g., after the user returns from settings).
     * Updates the [locationAvailabilityState].
     */
    fun refreshGpsStatus()

    /**
     * Checks if Google Play Services are available, which is necessary for FusedLocationProviderClient.
     * Updates the [locationAvailabilityState].
     */
    fun checkPlayServices()
}