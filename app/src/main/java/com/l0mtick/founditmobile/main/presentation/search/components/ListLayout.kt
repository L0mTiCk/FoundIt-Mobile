package com.l0mtick.founditmobile.main.presentation.search.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.main.presentation.home.components.SectionHeader
import com.l0mtick.founditmobile.main.presentation.search.SearchAction
import com.l0mtick.founditmobile.main.presentation.search.SearchState
import com.l0mtick.founditmobile.main.presentation.util.calculateDistanceBetweenPoints

@Composable
fun ListLayout(
    state: SearchState.ListScreen,
    onAction: (SearchAction) -> Unit,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLocation = state.userLocation
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding()
                    .padding(bottom = 12.dp)
            ) {
                SectionHeader(
                    header = R.string.lost_items,
                    description = null
                )
            }
        }

        item {
            OutlinedTextField(
                value = state.searchValue,
                onValueChange = {
                    onAction(SearchAction.OnListSearchValueChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .onFocusChanged { focusState ->
                        if (!focusState.hasFocus) {
                            Log.d("search_screen", "Focus lost, performing search")
                        }
                    },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    Log.d("search_screen", "On Ime.Done call")
                })
            )
        }

        item {
            CategoriesFilterRow(
                categories = state.categories,
                selectedCategories = state.selectedCategories,
                onCategoryClick = {
                    onAction(SearchAction.OnCategorySelect(it))
                },
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
            )
        }

        items(state.items.items, key = { it.id }) { item ->
            var distanceInMeters by remember(item.id) { mutableStateOf<Float?>(null) }

            LaunchedEffect(currentLocation, item.id) {
                distanceInMeters = calculateDistanceBetweenPoints(
                    userLocation = currentLocation,
                    itemLat = item.latitude,
                    itemLon = item.longitude
                )
            }

            BigItemCard(
                id = item.id,
                title = item.title,
                description = item.description ?: "No description",
                postedTimestamp = item.createdAt,
                imageUrl = item.photoUrls.firstOrNull(),
                distance = distanceInMeters,
                modifier = Modifier.padding(horizontal = 14.dp),
                onClick = onItemClick
            )
        }
    }
}