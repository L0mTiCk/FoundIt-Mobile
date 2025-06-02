package com.l0mtick.founditmobile.main.presentation.util

import android.location.Location

/**
 * Calculates distance between user location and point.
 *
 * @param userLocation user location
 * @param itemLat item latitude.
 * @param itemLon item longitude.
 * @return distance between in meters
 */
fun calculateDistanceBetweenPoints(
    userLocation: Location,
    itemLat: Double,
    itemLon: Double
): Float {
    val itemLocation = Location("itemLocation").apply {
        latitude = itemLat
        longitude = itemLon
    }
    return userLocation.distanceTo(itemLocation)
}