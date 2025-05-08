package com.l0mtick.founditmobile.main.presentation.search.components

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.main.presentation.search.SearchState
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.IconImage
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.style.expressions.dsl.generated.literal
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationSourceOptions
import com.mapbox.maps.plugin.annotation.ClusterOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.launch

@Composable
fun MapLayout(
    state: SearchState.MapScreen,
    modifier: Modifier = Modifier
) {
    var points by remember { mutableStateOf<List<Point>>(emptyList()) }
    val markerImage = rememberIconImage(key = "image_icon", painter = painterResource(R.drawable.telegram_plane_brands_solid))
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(
        state.items
    ) {
        coroutineScope.launch {
            points = state.items.items.map {
                Point.fromLngLat(
                    it.longitude, it.latitude
                )
            }
            Log.d("map_screen", points.toString())
        }
    }
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        scaleBar = { },
        mapViewportState = rememberMapViewportState {
            setCameraOptions {
                zoom(5.0)
                center(Point.fromLngLat(27.567444, 53.893009))
            }
        },
//        style = { MapStyle(style = "mapbox://styles/l0mtick/cmaf08ip400t701slcmm4bprl") },
    ) {
        MapEffect(Unit) { mapView ->
            mapView.location.updateSettings {
                locationPuck = createDefault2DPuck(withBearing = true)
                enabled = true
                puckBearing = PuckBearing.COURSE
                puckBearingEnabled = true
                pulsingEnabled = true
            }
        }
        PointAnnotationGroup(
            annotations = points.map {
                PointAnnotationOptions()
                    .withPoint(it)
            },
            annotationConfig = AnnotationConfig(
                annotationSourceOptions = AnnotationSourceOptions(
                    clusterOptions = ClusterOptions(
                        textColor = Color.WHITE,
                        textSize = 20.0,
                        circleRadiusExpression = literal(25.0),
                        colorLevels = listOf(
                            Pair(50, Color.RED),
                            Pair(10, Color.BLUE),
                            Pair(5, Color.YELLOW),
                            Pair(0, Color.GREEN)
                        )
                    )
                )
            )
        ) {
            iconImage = IconImage("fire-station")
            iconSize = 10.0
        }
    }
}