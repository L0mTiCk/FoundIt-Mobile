package com.l0mtick.founditmobile.main.presentation.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class InboxViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(InboxState())
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
            initialValue = InboxState()
        )

    fun onAction(action: InboxAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}