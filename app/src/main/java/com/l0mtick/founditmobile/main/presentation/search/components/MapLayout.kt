package com.l0mtick.founditmobile.main.presentation.search.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

@Composable
fun MapLayout(modifier: Modifier = Modifier) {
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        scaleBar = { },
        mapViewportState = rememberMapViewportState {
            setCameraOptions {
                zoom(5.0)
                center(Point.fromLngLat(27.567444, 53.893009))
            }
        }
    )
}