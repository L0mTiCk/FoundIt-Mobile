package com.l0mtick.founditmobile.main.presentation.additem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class AddItemViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(AddItemState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AddItemState()
        )

    fun onAction(action: AddItemAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}