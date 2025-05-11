package com.l0mtick.founditmobile.main.presentation.lostitemdetails

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastJoinToString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.defaultPlaceholder
import com.l0mtick.founditmobile.main.presentation.components.ItemImagesPager
import com.l0mtick.founditmobile.main.presentation.home.components.SectionHeader
import com.l0mtick.founditmobile.main.presentation.lostitemdetails.components.DetailsSectionCard
import com.l0mtick.founditmobile.main.presentation.lostitemdetails.components.ItemAuthorCard
import com.l0mtick.founditmobile.main.presentation.util.formatTimestampToShortDate
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme
import com.mapbox.geojson.Point
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.extension.compose.rememberMapState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.gestures.generated.GesturesSettings
import org.koin.androidx.compose.koinViewModel

@Composable
fun LostItemDetailsRoot(
    viewModel: LostItemDetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LostItemDetailsScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavBack = { }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun LostItemDetailsScreen(
    state: LostItemDetailsState,
    onAction: (LostItemDetailsAction) -> Unit,
    onNavBack: () -> Unit
) {
    val isPlaceholderVisible = state.lostItem == null
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(5.0)
        }
    }
    LaunchedEffect(state.lostItem) {
        Log.d("temp", state.lostItem.toString())
        if (state.lostItem != null) {
            mapViewportState.flyTo(
                cameraOptions = cameraOptions {
                    center(Point.fromLngLat(state.lostItem.longitude, state.lostItem.latitude))
                    zoom(8.0)
                },
                animationOptions = MapAnimationOptions.mapAnimationOptions { duration(2000) }
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding(),
        ) {
            IconButton(
                onClick = onNavBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Exit details",
                    tint = Theme.colors.onSurface
                )
            }
            SectionHeader(
                header = R.string.app_name,
                description = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        ItemImagesPager(
            imageUrls = state.lostItem?.photoUrls ?: listOf(""),
            modifier = Modifier.defaultPlaceholder(
                visible = isPlaceholderVisible,
                placeholderModifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = state.lostItem?.title ?: "",
            style = Theme.typography.headline,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .defaultPlaceholder(
                    visible = isPlaceholderVisible,
                    width = 100.dp,
                    placeholderModifier = Modifier.clip(RoundedCornerShape(2.dp))
                )
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = state.lostItem?.description ?: "",
            style = Theme.typography.body,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .defaultPlaceholder(
                    visible = isPlaceholderVisible,
                    width = 100.dp,
                    placeholderModifier = Modifier.clip(RoundedCornerShape(2.dp))
                ),
            lineHeight = 22.sp
        )
        Spacer(Modifier.height(16.dp))
        AnimatedVisibility(
            visible = state.owner != null
        ) {
            state.owner?.let { user ->
                ItemAuthorCard(
                    user = user,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(16.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Button(
                onClick = {},
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Theme.colors.brand,
                    contentColor = Theme.colors.onBrand
                )
            ) {
                Text("Chat")
            }
            Spacer(modifier = Modifier.requiredSize(width = 16.dp, height = 1.dp))
            Button(
                onClick = {},
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Theme.colors.onSurfaceVariant.copy(alpha = .2f),
                    contentColor = Theme.colors.onSurfaceVariant
                )
            ) {
                Text("Favorite")
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Details",
            style = Theme.typography.headline,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(12.dp))
        DetailsSectionCard(
            header = "Category",
            description = state.lostItem?.categories?.map { it.name }?.fastJoinToString()
                ?: "Empty",
            icon = Icons.Default.Search,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .defaultPlaceholder(
                    visible = isPlaceholderVisible,
                    placeholderModifier = Modifier.clip(RoundedCornerShape(8.dp))
                )
        )
        Spacer(Modifier.height(12.dp))
        DetailsSectionCard(
            header = "Publish date",
            description = formatTimestampToShortDate(state.lostItem?.createdAt ?: 0),
            icon = Icons.Default.DateRange,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .defaultPlaceholder(
                    visible = isPlaceholderVisible,
                    placeholderModifier = Modifier.clip(RoundedCornerShape(8.dp))
                )
        )
        Spacer(Modifier.height(12.dp))
        DetailsSectionCard(
            header = "Expire date",
            description = formatTimestampToShortDate(state.lostItem?.expiresAt ?: 0),
            icon = Icons.Default.DateRange,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .defaultPlaceholder(
                    visible = isPlaceholderVisible,
                    placeholderModifier = Modifier.clip(RoundedCornerShape(8.dp))
                )
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Location",
            style = Theme.typography.headline,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(16.dp))
        MapboxMap(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .heightIn(min = 400.dp, max = 500.dp),
            mapState = rememberMapState {
                gesturesSettings = GesturesSettings {
                    scrollEnabled = false
                    quickZoomEnabled = false
                    doubleTapToZoomInEnabled = false
                }
            },
            mapViewportState = mapViewportState,
            style = { MapStyle(style = "mapbox://styles/l0mtick/cmaf08ip400t701slcmm4bprl") },
            scaleBar = {}
        ) {
            state.lostItem?.let {
                CircleAnnotation(point = Point.fromLngLat(it.longitude, it.latitude)) {
                    circleRadius = 10.0
                    circleColor = androidx.compose.ui.graphics.Color.Magenta
                    circleBlur = .6
                    circleStrokeColor = androidx.compose.ui.graphics.Color.Yellow
                    circleStrokeWidth = 1.0
                }
            }
        }
        Spacer(Modifier.height(48.dp))
    }
}

@Preview
@Composable
private fun Preview() {
    FoundItMobileTheme {
        LostItemDetailsScreen(
            state = LostItemDetailsState(),
            onAction = {},
            onNavBack = {}
        )
    }
}