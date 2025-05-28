package com.l0mtick.founditmobile.main.presentation.search.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.OutlinedAppTextField
import com.l0mtick.founditmobile.main.presentation.home.components.SectionHeader
import com.l0mtick.founditmobile.main.presentation.search.SearchAction
import com.l0mtick.founditmobile.main.presentation.search.SearchState
import com.l0mtick.founditmobile.main.presentation.util.calculateDistanceBetweenPoints
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun ListLayout(
    state: SearchState.ListScreen,
    onAction: (SearchAction) -> Unit,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    var overscrollTrigger by remember { mutableStateOf(false) }
    var isOverscrolling by remember { mutableStateOf(false) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            var totalDrag = 0f

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (source == NestedScrollSource.UserInput && available.y < 0 && !lazyListState.canScrollForward) {
                    totalDrag += -available.y
                    isOverscrolling = true
                    if (totalDrag > 120f) {
                        overscrollTrigger = true
                    }
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (overscrollTrigger && !state.isLoadingMore) {
                    Log.d("ListLayout", "OVERSCROLL AT BOTTOM (SIMPLER HEADER): Trigger Load More")
                    onAction(SearchAction.OnLoadMoreListItems)
                }
                isOverscrolling = false
                overscrollTrigger = false
                totalDrag = 0f
                return Velocity.Zero
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                Column(
                    modifier = Modifier.background(Theme.colors.surface)
                ) {
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
                    OutlinedAppTextField(
                        value = state.searchValue,
                        onValueChange = {
                            onAction(SearchAction.OnListSearchValueChange(it))
                        },
                        label = stringResource(R.string.search_hint),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 20.dp)
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
            }

            item {
                CategoriesFilterRow(
                    categories = state.categories,
                    selectedCategories = state.selectedCategories,
                    onCategoryClick = {
                        onAction(SearchAction.OnCategorySelect(it))
                    },
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 20.dp)
                )
                DatePickerChip(
                    selectedDate = state.selectedDate,
                    onDateSelected = { timestamp ->
                        onAction(SearchAction.OnDateSelected(timestamp))
                    },
                    onDateCleared = {
                        onAction(SearchAction.OnDateCleared)
                    },
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 20.dp)
                )
            }

            items(state.items.items, key = { it.id }) { item ->
                var distanceInMeters by remember(
                    item.id,
                    state.userLocation
                ) { mutableStateOf<Float?>(null) }

                LaunchedEffect(state.userLocation, item.id) {
                    distanceInMeters = calculateDistanceBetweenPoints(
                        userLocation = state.userLocation,
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
            item {
                AnimatedVisibility(
                    visible = isOverscrolling || state.isLoadingMore,
                    enter = fadeIn() + slideInVertically { it / 2 },
                    exit = fadeOut() + slideOutVertically { it / 2 },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 56.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Theme.colors.brand
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            stringResource(if (state.isLoadingMore) R.string.loading_more else R.string.load_more),
                            style = Theme.typography.body,
                            color = Theme.colors.onSurface
                        )
                    }
                }
            }
        }
    }
}