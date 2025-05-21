package com.l0mtick.founditmobile.main.presentation.additem

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

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
            is AddItemAction.AddPhoto -> addPhoto(action.uri)
            AddItemAction.CenterOnUserLocation -> TODO()
            is AddItemAction.RemovePhoto -> removePhoto(action.uri)
            is AddItemAction.SelectCategory -> TODO()
            AddItemAction.SubmitItem -> TODO()
            AddItemAction.ToggleCategoryDropdown -> TODO()
            is AddItemAction.UpdateDescription -> updateDescription(action.description)
            is AddItemAction.UpdateTitle -> updateTitle(action.title)
        }
    }

    private fun addPhoto(uri: Uri) {
        _state.update {
            it.copy(
                selectedPhotos = it.selectedPhotos + uri
            )
        }
    }

    private fun removePhoto(uri: Uri) {
        _state.update {
            it.copy(
                selectedPhotos = it.selectedPhotos - uri
            )
        }
    }

    private fun updateDescription(value: String) {
        _state.update {
            it.copy(
                description = value
            )
        }
    }

    private fun updateTitle(value: String) {
        _state.update {
            it.copy(
                title = value
            )
        }
    }
}