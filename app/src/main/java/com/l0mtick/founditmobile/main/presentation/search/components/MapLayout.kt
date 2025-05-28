package com.l0mtick.founditmobile.main.presentation.search.components

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.gson.JsonParser
import com.l0mtick.founditmobile.common.presentation.util.ObserveAsEvents
import com.l0mtick.founditmobile.main.presentation.search.SearchAction
import com.l0mtick.founditmobile.main.presentation.search.SearchEvent
import com.l0mtick.founditmobile.main.presentation.search.SearchState
import com.l0mtick.founditmobile.ui.theme.Theme
import com.mapbox.geojson.Point
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.extension.style.expressions.dsl.generated.literal
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationSourceOptions
import com.mapbox.maps.plugin.annotation.ClusterOptions
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.flow.Flow

@Composable
fun MapLayout(
    state: SearchState.MapScreen,
    onNavigateToDetails: (Int) -> Unit,
    events: Flow<SearchEvent>,
    onAction: (SearchAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var isDropDownOpened by remember { mutableStateOf(false) }
    var dropDownItemId by remember { mutableStateOf<Int?>(null) }
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(8.0)
        }
    }

    ObserveAsEvents(
        flow = events
    ) { event ->
        when (event) {
            is SearchEvent.MapLayoutEvent -> {
                when (event) {
                    is SearchEvent.MapLayoutEvent.CenterOnUserLocation -> {
                        mapViewportState.flyTo(
                            cameraOptions {
                                center(
                                    Point.fromLngLat(
                                        event.location.longitude,
                                        event.location.latitude
                                    )
                                )
                                zoom(8.0)
                            },
                            animationOptions = MapAnimationOptions.mapAnimationOptions {
                                duration(
                                    2000
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
    ) {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            scaleBar = { },
            mapViewportState = mapViewportState,
            style = { MapStyle(style = "mapbox://styles/l0mtick/cmaf08ip400t701slcmm4bprl") },
        ) {
            MapEffect(Unit) { mapView ->

                mapView.location.apply {
                    locationPuck = createDefault2DPuck(withBearing = true)
                    enabled = true
                    puckBearing = PuckBearing.COURSE
                    puckBearingEnabled = true
                    pulsingEnabled = true
                }

                onAction(SearchAction.OnCenterOnUser)
            }
            CircleAnnotationGroup(
                annotations = state.items.map {
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
                    originalId?.let { id ->
                        dropDownItemId = id
                        mapViewportState.flyTo(
                            cameraOptions = cameraOptions {
                                center(it.point)
                                zoom(11.0)
                            },
                            animationOptions = MapAnimationOptions.mapAnimationOptions {
                                duration(2000)
                            }
                        )
                        isDropDownOpened = true
                    }
                    true
                }
            }
        }
        FloatingActionButton(
            onClick = {
                onAction(SearchAction.OnCenterOnUser)
            },
            containerColor = Theme.colors.brand,
            contentColor = Theme.colors.onBrand,
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(Icons.Default.LocationOn, "Reset location")
        }
    }
    if (isDropDownOpened) {
        state.items.firstOrNull { it.id == dropDownItemId }?.let { item ->
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