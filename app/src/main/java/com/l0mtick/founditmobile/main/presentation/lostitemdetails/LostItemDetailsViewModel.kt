package com.l0mtick.founditmobile.main.presentation.lostitemdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.main.domain.repository.LostItemRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LostItemDetailsViewModel(
    private val itemRepository: LostItemRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val route = savedStateHandle.toRoute<NavigationRoute.Main.ItemDetails>()
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(LostItemDetailsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                viewModelScope.launch {
                    delay(5000)
                    val item = itemRepository.getDetailedLostItem(itemId = route.itemId)
                    when(item) {
                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    lostItem = item.data.first,
                                    owner = item.data.second
                                )
                            }
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
            else -> TODO("Handle actions")
        }
    }

}