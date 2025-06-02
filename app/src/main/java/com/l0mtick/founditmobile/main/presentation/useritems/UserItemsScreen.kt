package com.l0mtick.founditmobile.main.presentation.useritems

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.main.domain.model.LostItem
import com.l0mtick.founditmobile.main.presentation.components.ConfirmationDialog
import com.l0mtick.founditmobile.main.presentation.home.components.SectionHeader
import com.l0mtick.founditmobile.main.presentation.search.components.CompactItemCard
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserItemsRoot(
    navController: NavController,
    viewModel: UserItemsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    UserItemsScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavBack = {
            navController.navigateUp()
        },
        onNavToItem = { itemId ->
            navController.navigate(NavigationRoute.Main.ItemDetails(itemId))
        }
    )
}

@Composable
fun UserItemsScreen(
    state: UserItemsState,
    onAction: (UserItemsAction) -> Unit,
    onNavBack: () -> Unit,
    onNavToItem: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var isConfirmationShown by remember { mutableStateOf(false) }
        var isMarkAsReturnedConfirmationShown by remember { mutableStateOf(false) }
        var itemIdToOperate by remember { mutableStateOf<Int?>(null) }

        // Header with back button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
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

            // Title based on the current state
            val titleResId = if (state is UserItemsState.UserCreatedItemsState) {
                R.string.my_items
            } else {
                R.string.favorite_items
            }

            SectionHeader(
                header = titleResId,
                description = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Content based on the current state
        when (state) {

            is UserItemsState.UserCreatedItemsState -> {
                if (state.isLoading) {
                    Box(
                        Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            color = Theme.colors.brand,
                            strokeWidth = 2.dp,
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.Center)
                        )
                    }
                } else if (state.items.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = stringResource(R.string.no_items_created),
                            modifier = Modifier.align(Alignment.Center),
                            style = Theme.typography.body,
                            color = Theme.colors.onSurfaceVariant
                        )
                    }
                } else {
                    ItemsList(
                        items = state.items,
                        isUserCreated = true,
                        onItemClick = onNavToItem,
                        onActionClick = { itemId ->
                            itemIdToOperate = itemId
                            isConfirmationShown = true
                        },
                        onMarkAsReturned = { itemId ->
                            itemIdToOperate = itemId
                            isMarkAsReturnedConfirmationShown = true
                        }
                    )
                }
                if (isConfirmationShown) {
                    ConfirmationDialog(
                        title = stringResource(R.string.dialog_delete_item_title),
                        message = stringResource(R.string.dialog_delete_item_message),
                        confirmButtonText = stringResource(R.string.dialog_delete_item_confirm),
                        dismissButtonText = stringResource(R.string.dialog_delete_item_cancel),
                        onConfirm = {
                            itemIdToOperate?.let {
                                onAction(
                                    UserItemsAction.DeleteItem(it)
                                )
                            }
                            itemIdToOperate = null
                            isConfirmationShown = false
                        },
                        onDismiss = {
                            itemIdToOperate = null
                            isConfirmationShown = false
                        }
                    )
                }
                if (isMarkAsReturnedConfirmationShown) {
                    ConfirmationDialog(
                        title = stringResource(R.string.dialog_return_item_title),
                        message = stringResource(R.string.dialog_return_item_message),
                        confirmButtonText = stringResource(R.string.dialog_return_item_confirm),
                        dismissButtonText = stringResource(R.string.cancel),
                        onConfirm = {
                            itemIdToOperate?.let {
                                onAction(
                                    UserItemsAction.MarkAsReturned(it)
                                )
                            }
                            itemIdToOperate = null
                            isMarkAsReturnedConfirmationShown = false
                        },
                        onDismiss = {
                            itemIdToOperate = null
                            isMarkAsReturnedConfirmationShown = false
                        }
                    )
                }
            }

            is UserItemsState.UserFavoriteItemsState -> {
                if (state.isLoading) {
                    Box(
                        Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            color = Theme.colors.brand,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(48.dp).align(Alignment.Center)
                        )
                    }
                } else if (state.items.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = stringResource(R.string.no_favorite_items),
                            modifier = Modifier.align(Alignment.Center),
                            style = Theme.typography.body,
                            color = Theme.colors.onSurfaceVariant
                        )
                    }
                } else {
                    ItemsList(
                        items = state.items,
                        isUserCreated = false,
                        onItemClick = onNavToItem,
                        onActionClick = { itemId ->
                            itemIdToOperate = itemId
                            isConfirmationShown = true
                        }
                    )
                }
                if (isConfirmationShown) {
                    ConfirmationDialog(
                        title = stringResource(R.string.dialog_remove_from_favorites_title),
                        message = stringResource(R.string.dialog_remove_from_favorites_message),
                        confirmButtonText = stringResource(R.string.dialog_remove_from_favorites_confirm),
                        dismissButtonText = stringResource(R.string.dialog_remove_from_favorites_cancel),
                        onConfirm = {
                            itemIdToOperate?.let {
                                onAction(
                                    UserItemsAction.RemoveFromFavorites(
                                        it
                                    )
                                )
                            }
                            itemIdToOperate = null
                            isConfirmationShown = false
                        },
                        onDismiss = {
                            itemIdToOperate = null
                            isConfirmationShown = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemsList(
    items: List<LostItem>,
    isUserCreated: Boolean,
    onItemClick: (Int) -> Unit,
    onActionClick: (Int) -> Unit,
    onMarkAsReturned: (Int) -> Unit = {}
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { item ->
            CompactItemCard(
                id = item.id,
                title = item.title,
                description = item.description ?: "",
                postedTimestamp = item.createdAt,
                imageUrl = item.photoUrls.firstOrNull(),
                isUserCreated = isUserCreated,
                onClick = { onItemClick(item.id) },
                onActionClick = { onActionClick(item.id) },
                status = item.status,
                onMarkAsReturned = { onMarkAsReturned(item.id) },
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    FoundItMobileTheme {
        UserItemsScreen(
            state = UserItemsState.UserFavoriteItemsState(),
            onAction = {},
            onNavBack = {},
            onNavToItem = {}
        )
    }
}