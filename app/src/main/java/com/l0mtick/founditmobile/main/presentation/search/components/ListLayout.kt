package com.l0mtick.founditmobile.main.presentation.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.FadeVisibility
import com.l0mtick.founditmobile.common.presentation.components.OutlinedAppTextField
import com.l0mtick.founditmobile.main.presentation.home.components.SectionHeader
import com.l0mtick.founditmobile.main.presentation.search.SearchAction
import com.l0mtick.founditmobile.main.presentation.search.SearchState
import com.l0mtick.founditmobile.main.presentation.util.calculateDistanceBetweenPoints
import com.l0mtick.founditmobile.ui.theme.Theme
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class
)
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
    val coroutineScope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val imeVisible = WindowInsets.isImeVisible
    val wasImeVisible = remember { mutableStateOf(false) }

    val firstTimeEntering = remember { mutableStateOf(true) }
    val lastSearchValue = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        snapshotFlow { state.searchValue }.collect {
            firstTimeEntering.value = false
        }
    }

    LaunchedEffect(imeVisible) {
        if (!imeVisible && wasImeVisible.value) {
            if (!firstTimeEntering.value &&
                state.searchValue != lastSearchValue.value
            ) {
                lastSearchValue.value = state.searchValue
                onAction(SearchAction.OnPerformSearch)
            }
        }

        wasImeVisible.value = imeVisible
    }

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
                    onAction(SearchAction.OnLoadMoreListItems)
                }
                isOverscrolling = false
                overscrollTrigger = false
                totalDrag = 0f
                return Velocity.Zero
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = false,
        state = pullToRefreshState,
        onRefresh = {
            coroutineScope.launch {
                pullToRefreshState.animateToHidden()
                onAction(SearchAction.OnPerformSearch)
            }
        },
        indicator = {
            PullToRefreshDefaults.Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState,
                isRefreshing = state.isLoading,
                containerColor = Theme.colors.brandMuted,
                color = Theme.colors.brand
            )
        }
    ) {
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        ) {
                            LookaheadScope {
                                OutlinedAppTextField(
                                    value = state.searchValue,
                                    onValueChange = {
                                        onAction(SearchAction.OnListSearchValueChange(it))
                                    },
                                    label = stringResource(R.string.search_hint),
                                    modifier = Modifier
                                        .padding(vertical = 4.dp,)
                                        .weight(1f)
                                        .animateBounds(this),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    keyboardActions = KeyboardActions(onDone = {
                                        keyboardController?.hide()
                                        focusManager.clearFocus(force = true)

                                        if (!firstTimeEntering.value && state.searchValue != lastSearchValue.value) {
                                            lastSearchValue.value = state.searchValue
                                            onAction(SearchAction.OnPerformSearch)
                                        }
                                    })
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            FadeVisibility(
                                visible = state.searchValue.isNotEmpty(),
                            ) {
                                IconButton(
                                    onClick = {
                                        onAction(SearchAction.OnListSearchValueChange(""))
                                        keyboardController?.hide()
                                        focusManager.clearFocus(force = true)
                                        if (!imeVisible) {
                                            lastSearchValue.value = state.searchValue
                                            onAction(SearchAction.OnPerformSearch)
                                        }
                                    },
                                    modifier = Modifier.requiredSize(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = Theme.colors.onSurface
                                    )
                                }
                            }
                        }

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
                if (state.items.items.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillParentMaxHeight(.8f)
                                .fillMaxWidth()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.empty_search),
                                style = Theme.typography.title,
                                color = Theme.colors.onSurface
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = stringResource(R.string.empty_search_hint),
                                style = Theme.typography.body,
                                color = Theme.colors.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
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
}