package com.l0mtick.founditmobile.main.presentation.useritems

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarManager
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarType
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.common.presentation.util.UiText
import com.l0mtick.founditmobile.main.domain.repository.LostItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserItemsViewModel(
    private val lostItemRepository: LostItemRepository,
    private val snackbarManager: SnackbarManager,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val route = savedStateHandle.toRoute<NavigationRoute.Main.UserItems>()


    private val _state =
        MutableStateFlow<UserItemsState>(
            if (route.isFavorite) UserItemsState.UserFavoriteItemsState()
            else UserItemsState.UserCreatedItemsState()
        )
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun onAction(action: UserItemsAction) {
        when (action) {
            is UserItemsAction.DeleteItem -> handleDeleteItem(action.itemId)
            is UserItemsAction.RemoveFromFavorites -> handleRemoveFromFavorites(action.itemId)
            is UserItemsAction.MarkAsReturned -> handleMarkAsReturned(action.itemId)
        }
    }

    private fun handleDeleteItem(itemId: Int) {
        viewModelScope.launch {
            when (lostItemRepository.deleteUserCreatedItem(itemId)) {
                is Result.Success -> {
                    snackbarManager.showSnackbar(
                        UiText.StringResource(R.string.delete_user_item_success),
                        SnackbarType.SUCCESS
                    )
                    loadData()
                }

                is Result.Error -> {
                    snackbarManager.showSnackbar(
                        UiText.StringResource(R.string.delete_user_item_error),
                        SnackbarType.ERROR
                    )
                }
            }
        }
    }

    private fun handleRemoveFromFavorites(itemId: Int) {
        viewModelScope.launch {
            when (lostItemRepository.removeItemFromFavorites(itemId)) {
                is Result.Success -> {
                    snackbarManager.showSnackbar(
                        UiText.StringResource(R.string.delete_favorite_item_success),
                        SnackbarType.SUCCESS
                    )
                    loadData()
                }

                is Result.Error -> {
                    snackbarManager.showSnackbar(
                        UiText.StringResource(R.string.delete_favorite_item_error),
                        SnackbarType.ERROR
                    )
                }
            }
        }
    }

    private fun handleMarkAsReturned(itemId: Int) {
        viewModelScope.launch {
            when(lostItemRepository.markItemAsReturned(itemId)) {
                is Result.Success -> {
                    snackbarManager.showSnackbar(
                        UiText.StringResource(R.string.mark_as_returned_item_success),
                        SnackbarType.SUCCESS
                    )
                    loadData()
                }
                is Result.Error -> {
                    snackbarManager.showSnackbar(
                        UiText.StringResource(R.string.marl_as_returned_item_error),
                        SnackbarType.ERROR
                    )
                }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            when (val current = _state.value) {
                is UserItemsState.UserFavoriteItemsState -> {
                    when (val result = lostItemRepository.getFavoriteLostItems()) {
                        is Result.Success -> {
                            _state.update {
                                current.copy(
                                    items = result.data
                                )
                            }
                        }

                        is Result.Error -> {
                            snackbarManager.showError(result.error)
                        }
                    }
                }

                is UserItemsState.UserCreatedItemsState -> {
                    when (val result = lostItemRepository.getUserCreatedLostItems()) {
                        is Result.Success -> {
                            _state.update {
                                current.copy(
                                    items = result.data
                                )
                            }
                        }

                        is Result.Error -> {
                            snackbarManager.showError(result.error)
                        }
                    }
                }
            }
        }
    }
}
