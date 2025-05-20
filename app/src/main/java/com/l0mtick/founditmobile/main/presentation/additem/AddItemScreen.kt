package com.l0mtick.founditmobile.main.presentation.additem

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.OutlinedAppTextField
import com.l0mtick.founditmobile.common.presentation.components.PrimaryButton
import com.l0mtick.founditmobile.main.domain.model.Category
import com.l0mtick.founditmobile.main.presentation.home.components.SectionHeader
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
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
                    contentDescription = "Назад",
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
        
        // Секция фотографий
        Text(
            text = "Фотографии",
            style = Theme.typography.title,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        
        Spacer(Modifier.height(16.dp))
        
        // Компонент для отображения и добавления фотографий
        ItemImagesSelector(
            selectedPhotos = state.selectedPhotos,
            onPhotoSelected = { /* Здесь будет обработка выбора фото */ },
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        
        Spacer(Modifier.height(24.dp))
        
        // Поле для названия
        Text(
            text = "Информация",
            style = Theme.typography.title,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        
        Spacer(Modifier.height(16.dp))
        
        OutlinedAppTextField(
            value = state.title,
            onValueChange = { /* Здесь будет обновление названия */ },
            label = "Название",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        
        Spacer(Modifier.height(16.dp))
        
        // Поле для описания
        OutlinedTextField(
            value = state.description,
            onValueChange = { /* Здесь будет обновление описания */ },
            label = { Text("Описание") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 24.dp),
            singleLine = false,
            maxLines = 5
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
                    text = state.selectedCategory?.name ?: "Выберите категорию",
                    style = Theme.typography.body,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Открыть список категорий"
                )
            }
            
            DropdownMenu(
                expanded = state.isCategoryDropdownExpanded,
                onDismissRequest = { /* Здесь будет закрытие выпадающего списка */ },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                state.categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = { /* Здесь будет выбор категории */ }
                    )
                }
            }
        }
        
        Spacer(Modifier.height(24.dp))
        
        // Карта для отображения местоположения
        Text(
            text = "Местоположение",
            style = Theme.typography.title,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        
        Spacer(Modifier.height(16.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 24.dp)
        ) {
            val mapViewportState = rememberMapViewportState {
                setCameraOptions {
                    zoom(14.0)
                    center(Point.fromLngLat(state.userLongitude, state.userLatitude))
                }
            }
            
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = mapViewportState,
                scaleBar = {},
                style = { MapStyle(style = "mapbox://styles/l0mtick/cmaf08ip400t701slcmm4bprl") },
            ) {
                // Маркер на карте
                PointAnnotationGroup(
                    annotations = listOf(
                        PointAnnotationOptions()
                            .withPoint(Point.fromLngLat(state.userLongitude, state.userLatitude))
                    )
                )
            }
            
            // Кнопка для центрирования на пользователе
            FloatingActionButton(
                onClick = { /* Здесь будет центрирование на пользователе */ },
                containerColor = Theme.colors.brand,
                contentColor = Theme.colors.onBrand,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Icon(Icons.Default.LocationOn, "Моё местоположение")
            }
        }
        
        Spacer(Modifier.height(24.dp))
        
        // Кнопка добавления метки
        PrimaryButton(
            text = "Добавить метку",
            onClick = { /* Здесь будет добавление метки */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        
        Spacer(Modifier.height(48.dp))
    }
}

@Composable
fun ItemImagesSelector(
    selectedPhotos: List<Uri>,
    onPhotoSelected: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onPhotoSelected(it) }
    }
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Кнопка добавления фото
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Theme.colors.brandMuted)
                .clickable { photoPickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Добавить фото",
                tint = Theme.colors.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
        
        selectedPhotos.take(4).forEach { uri ->
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = uri,
                    contentDescription = "Выбранное фото",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        
        // Индикатор дополнительных фотографий
        if (selectedPhotos.size > 4) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Theme.colors.brandMuted),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+${selectedPhotos.size - 4}",
                    style = Theme.typography.body,
                    color = Theme.colors.onSurfaceVariant
                )
            }
        }
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