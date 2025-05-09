package com.l0mtick.founditmobile.main.presentation.search.components

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.gson.JsonParser
import com.l0mtick.founditmobile.main.presentation.search.SearchState
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.extension.style.expressions.dsl.generated.literal
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationSourceOptions
import com.mapbox.maps.plugin.annotation.ClusterOptions
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun MapLayout(
    state: SearchState.MapScreen,
    onNavigateToDetails: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isDropDownOpened by remember { mutableStateOf(false) }
    var dropDownItemId by remember { mutableStateOf<Int?>(null) }
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        scaleBar = { },
        mapViewportState = rememberMapViewportState {
            setCameraOptions {
                zoom(5.0)
                center(Point.fromLngLat(27.567444, 53.893009))
            }
        },
        style = { MapStyle(style = "mapbox://styles/l0mtick/cmaf08ip400t701slcmm4bprl") },
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
        CircleAnnotation(point = Point.fromLngLat(28.567444, 53.893009)) {
            circleRadius = 8.0
            circleColor = androidx.compose.ui.graphics.Color.Cyan
            circleStrokeWidth = 2.0
            circleStrokeColor = androidx.compose.ui.graphics.Color.Yellow
        }
        CircleAnnotationGroup(
            annotations = state.items.items.map {
                CircleAnnotationOptions()
                    .withPoint(Point.fromLngLat(it.longitude, it.latitude))
                    .withData(
                        JsonParser.parseString("""{"lostItemId":${it.id}}""").asJsonObject
                    )
            },
            annotationConfig = AnnotationConfig(
                annotationSourceOptions = AnnotationSourceOptions(
                    clusterOptions = ClusterOptions(
                        textColor = Color.WHITE,
                        textSize = 20.0,
                        circleRadiusExpression = literal(25.0),
                        colorLevels = listOf(
                            Pair(50, Color.RED),
                            Pair(20, Color.CYAN),
                            Pair(5, Color.YELLOW),
                            Pair(1, Color.MAGENTA),
                        )
                    )
                )
            )
        ) {
            circleRadius = 10.0
            circleColor = androidx.compose.ui.graphics.Color.Magenta
            circleBlur = .6
            circleStrokeColor = androidx.compose.ui.graphics.Color.Yellow
            circleStrokeWidth = 1.0
            interactionsState.onClicked {
                val originalId = it.getData()?.asJsonObject?.get("lostItemId")?.asInt
                Log.d("map_screen", originalId.toString())
                originalId?.let {
                    dropDownItemId = it
                    isDropDownOpened = true
                }
                true
            }
        }
    }
    if (isDropDownOpened) {
        state.items.items.firstOrNull { it.id == dropDownItemId }?.let { item ->
            MapModalBottomSheet(
                item = item,
                onDismissRequest = {
                    isDropDownOpened = false
                },
                onViewDetailsClick = { id ->
                    onNavigateToDetails(id)
                }
            )
        } ?: { isDropDownOpened = false }
    }
}