package com.l0mtick.founditmobile.main.presentation.additem

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.FadeVisibility
import com.l0mtick.founditmobile.common.presentation.components.OutlinedAppTextField
import com.l0mtick.founditmobile.common.presentation.components.PrimaryButton
import com.l0mtick.founditmobile.common.presentation.util.asUiText
import com.l0mtick.founditmobile.main.domain.model.Category
import com.l0mtick.founditmobile.main.presentation.additem.components.EditableImagesPager
import com.l0mtick.founditmobile.main.presentation.home.components.SectionHeader
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.rememberMapState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.gestures.generated.GesturesSettings
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddItemRoot(
    onNavBack: () -> Unit,
    viewModel: AddItemViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AddItemScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavBack = onNavBack
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AddItemScreen(
    state: AddItemState,
    onAction: (AddItemAction) -> Unit,
    onNavBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Заголовок
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
                    contentDescription = "Navigate back",
                    tint = Theme.colors.onSurface
                )
            }
            SectionHeader(
                header = R.string.add_item_title,
                description = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.photos),
            style = Theme.typography.title,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(16.dp))

        EditableImagesPager(
            imagesUri = state.selectedPhotos,
            onImageAdd = {
                onAction(AddItemAction.AddPhoto(it))
            },
            onImageRemove = {
                onAction(AddItemAction.RemovePhoto(it))
            },
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.information),
            style = Theme.typography.title,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(16.dp))

        OutlinedAppTextField(
            value = state.title.value,
            onValueChange = { onAction(AddItemAction.UpdateTitle(it)) },
            label = stringResource(R.string.title),
            isError = state.title.isError,
            errorText = state.title.errors.firstOrNull()?.asUiText()?.asString(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        OutlinedTextField(
            value = state.description.value,
            onValueChange = { onAction(AddItemAction.UpdateDescription(it)) },
            label = { Text(stringResource(R.string.description)) },
            isError = state.description.isError,
            supportingText = {
                Row {
                    Text(
                        text = state.description.errors.firstOrNull()?.asUiText()?.asString() ?: ""
                    )
                    Spacer(Modifier.weight(1f))
                    FadeVisibility(state.description.value.length >= 200) {
                        Text(
                            text = "${state.description.value.length}/255"
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .animateContentSize(),
            singleLine = false,
            maxLines = 5,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Theme.colors.onSurface,
                focusedBorderColor = Theme.colors.brand,
                focusedLabelColor = Theme.colors.brand,
                cursorColor = Theme.colors.brand,
                selectionColors = TextSelectionColors(
                    handleColor = Theme.colors.brand,
                    backgroundColor = Theme.colors.brandMuted
                )
            )
        )


        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Theme.colors.onSurfaceVariant,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable { /* Здесь будет открытие/закрытие выпадающего списка */ }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = state.selectedCategory?.name ?: stringResource(R.string.select_category),
                    style = Theme.typography.body,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = ""
                )
            }

            DropdownMenu(
                expanded = state.isCategoryDropdownExpanded,
                onDismissRequest = { },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                state.categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = { }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.location),
            style = Theme.typography.title,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            val mapViewportState = rememberMapViewportState {
                setCameraOptions {
                    zoom(11.0)
                }
            }

            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = mapViewportState,
                mapState = rememberMapState {
                    gesturesSettings = GesturesSettings {
                        scrollEnabled = false
                        quickZoomEnabled = false
                        doubleTapToZoomInEnabled = false
                        doubleTouchToZoomOutEnabled = false
                        pitchEnabled = false
                        pitchEnabled = false
                        rotateEnabled = false
                    }
                },
                scaleBar = {},
                style = { MapStyle(style = "mapbox://styles/l0mtick/cmaf08ip400t701slcmm4bprl") },
            ) {
                MapEffect(Unit) { mapView ->

                    val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
                        mapViewportState.flyTo(CameraOptions.Builder().center(it).build())
                        mapView.gestures.focalPoint = mapView.mapboxMap.pixelForCoordinate(it)
                    }

                    mapView.location.apply {
                        locationPuck = createDefault2DPuck(withBearing = true)
                        enabled = true
                        puckBearing = PuckBearing.COURSE
                        puckBearingEnabled = true
                        pulsingEnabled = true
                        addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Кнопка добавления метки
        PrimaryButton(
            text = stringResource(R.string.submit_item),
            onClick = { /* Здесь будет добавление метки */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    FoundItMobileTheme {
        AddItemScreen(
            state = AddItemState(
                categories = listOf(
                    Category(1, "Электроника", ""),
                    Category(2, "Документы", ""),
                    Category(3, "Одежда", "")
                )
            ),
            onAction = {},
            onNavBack = {}
        )
    }
}