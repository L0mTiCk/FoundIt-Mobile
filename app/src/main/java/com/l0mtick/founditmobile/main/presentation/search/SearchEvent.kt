package com.l0mtick.founditmobile.main.presentation.search

import android.location.Location

sealed interface SearchEvent {
    sealed interface MapLayoutEvent : SearchEvent {
        data class CenterOnUserLocation(val location: Location) : MapLayoutEvent
    }
}