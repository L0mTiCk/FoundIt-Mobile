package com.l0mtick.founditmobile.main.presentation.lostitemdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarManager
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarType
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.UserSessionManager
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.common.presentation.util.UiText
import com.l0mtick.founditmobile.main.domain.repository.LostItemRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LostItemDetailsViewModel(
    private val itemRepository: LostItemRepository,
    private val userSessionManager: UserSessionManager,
    private val snackbarManager: SnackbarManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val route = savedStateHandle.toRoute<NavigationRoute.Main.ItemDetails>()
    private var hasLoadedInitialData = false

    private val eventChannel = Channel<LostItemDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(LostItemDetailsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                viewModelScope.launch {
                    _state.update {
                        it.copy(isGuest = userSessionManager.isGuestUser.value)
                    }
                    delay(1000)
                    val item = itemRepository.getDetailedLostItem(itemId = route.itemId)
                    when (item) {
                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    lostItem = item.data.first,
                                    owner = item.data.second
                                )
                            }
                            Log.d("details_viewmodel", item.data.first.toString())
                        }

                        is Result.Error -> {
                            Log.e("details_viewmodel", item.error.toString())
                        }
                    }
                    hasLoadedInitialData = true
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LostItemDetailsState()
        )

    fun onAction(action: LostItemDetailsAction) {
        when (action) {
            LostItemDetailsAction.OnAddToFavorites -> addToFavorites()
            LostItemDetailsAction.OnCreateChatForItem -> createChat()
        }
    }

    private fun addToFavorites() {
        _state.value.lostItem?.id?.let { id ->
            viewModelScope.launch {
                when (itemRepository.addItemToFavorites(id)) {
                    is Result.Success<*, *> -> {
                        snackbarManager.showSnackbar(
                            UiText.StringResource(R.string.details_add_fav_succeess),
                            SnackbarType.SUCCESS
                        )
                    }

                    is Result.Error<*, *> -> {
                        snackbarManager.showSnackbar(
                            UiText.StringResource(R.string.details_add_fav_error),
                            SnackbarType.ERROR
                        )
                    }
                }
            }
        } ?: {
            viewModelScope.launch {
                snackbarManager.showSnackbar(
                    UiText.StringResource(R.string.details_not_loaded_error),
                    SnackbarType.ERROR
                )
            }
        }
    }

    private fun createChat() {
        _state.value.lostItem?.let { item ->
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        isChatLoading = true
                    )
                }
                val result = itemRepository.createChatForItem(
                    userId = item.userId,
                    itemId = item.id
                )
                when (result) {
                    is Result.Success -> {
                        snackbarManager.showSuccess(
                            UiText.StringResource(R.string.details_created_chat_error)
                        )
                        _state.update { it.copy(isChatLoading = false) }
                        eventChannel.send(LostItemDetailsEvent.NavigateToChat(result.data.id))
                    }

                    is Result.Error -> {
                        when(result.error) {
                            DataError.Network.CONFLICT -> {
                                snackbarManager.showSnackbar(
                                    UiText.StringResource(R.string.details_not_created_chat_conflict_error),
                                    SnackbarType.ERROR
                                )
                            }
                            else -> {
                                snackbarManager.showSnackbar(
                                    UiText.StringResource(R.string.details_not_created_chat_error),
                                    SnackbarType.ERROR
                                )
                            }
                        }
                        _state.update {
                            it.copy(
                                isChatLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

}