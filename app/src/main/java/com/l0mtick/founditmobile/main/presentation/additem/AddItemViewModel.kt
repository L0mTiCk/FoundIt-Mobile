package com.l0mtick.founditmobile.main.presentation.additem

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarManager
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarType
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.ValidationManager
import com.l0mtick.founditmobile.common.presentation.util.isValid
import com.l0mtick.founditmobile.common.presentation.util.updateAndValidateTextFieldInState
import com.l0mtick.founditmobile.main.domain.model.Category
import com.l0mtick.founditmobile.main.domain.repository.AddItemRepository
import com.l0mtick.founditmobile.main.domain.repository.CategoriesRepository
import com.l0mtick.founditmobile.main.domain.repository.LocationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddItemViewModel(
    private val validator: ValidationManager,
    private val categoriesRepository: CategoriesRepository,
    private val addItemRepository: AddItemRepository,
    private val locationService: LocationService,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(AddItemState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                viewModelScope.launch {
                    launch {
                        _state.update {
                            it.copy(
                                publishLimit = (addItemRepository.getUserLevel() ?: 1) * 3f
                            )
                        }
                    }
                    when(val categories = categoriesRepository.getAllCategories()) {
                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    categories = categories.data
                                )
                            }
                        }
                        is Result.Error -> {
                            Log.e("add_viewmodel", categories.toString())
                        }
                    }
                }
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
            is AddItemAction.RemovePhoto -> removePhoto(action.uri)
            is AddItemAction.SelectCategory -> selectCategory(action.category)
            AddItemAction.SubmitItem -> submit()
            is AddItemAction.UpdateDescription -> updateDescription(action.description)
            is AddItemAction.UpdateTitle -> updateTitle(action.title)
            is AddItemAction.UpdatePublishTime -> updatePublishTime(action.value)
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
        updateAndValidateTextFieldInState<AddItemState>(
            stateFlow = _state,
            getField = { it.description },
            setField = { state, field -> state.copy(description = field) },
            newValue = value,
            validate = validator::validateItemDescription,
        )
    }

    private fun updateTitle(value: String) {
        updateAndValidateTextFieldInState<AddItemState>(
            stateFlow = _state,
            getField = { it.title },
            setField = { state, field -> state.copy(title = field) },
            newValue = value,
            validate = validator::validateItemTitle,
        )
    }

    private fun selectCategory(category: Category) {
        _state.update {
            it.copy(
                selectedCategory = category
            )
        }
    }

    private fun updatePublishTime(value: Float) {
        _state.update {
            it.copy(
                publishTime = value
            )
        }
    }

    private fun submit() {
        val currentState = _state.value
        
        if (!currentState.title.isValid()) {
            viewModelScope.launch {
                snackbarManager.showSnackbar("Title is required", type = SnackbarType.ERROR)
            }
            return
        }
        
        if (!currentState.description.isValid()) {
            viewModelScope.launch {
                snackbarManager.showSnackbar("Description is required", type = SnackbarType.ERROR)
            }
            return
        }
        
        if (currentState.selectedCategory == null) {
            viewModelScope.launch {
                snackbarManager.showSnackbar("Please select a category", type = SnackbarType.ERROR)
            }
            return
        }
        
        if (currentState.selectedPhotos.isEmpty()) {
            viewModelScope.launch {
                snackbarManager.showSnackbar("Please add at least one photo", type = SnackbarType.ERROR)
            }
            return
        }
        
        _state.update {
            it.copy(isSubmitting = true)
        }
        
        viewModelScope.launch {
            try {
                val locationResult = locationService.getCurrentLocation()
                if (locationResult !is Result.Success) {
                    snackbarManager.showSnackbar("Failed to get location. Please check GPS and permissions.", type = SnackbarType.ERROR)
                    _state.update { it.copy(isSubmitting = false) }
                    return@launch
                }
                
                val location = locationResult.data
                
                val currentTimeMillis = System.currentTimeMillis()
                val daysToAdd = currentState.publishTime.toLong()
                val expiresAt = currentTimeMillis + (daysToAdd * 24 * 60 * 60 * 1000)
                
                val createResult = addItemRepository.createItem(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    title = currentState.title.value,
                    description = currentState.description.value,
                    expiresAt = expiresAt,
                    categoryIds = listOf(currentState.selectedCategory!!.id.toInt())
                )
                
                when (createResult) {
                    is Result.Success -> {
                        val itemId = createResult.data
                        
                        var uploadSuccess = true
                        for (photoUri in currentState.selectedPhotos) {
                            when (val uploadResult = addItemRepository.uploadItemPhoto(itemId, photoUri)) {
                                is Result.Success -> {
                                    //TODO
                                    Log.d("add_viewmodel", uploadResult.data)
                                }
                                is Result.Error -> {
                                    uploadSuccess = false
                                    snackbarManager.showSnackbar("Failed to upload photo. Item created but some photos may be missing.", type = SnackbarType.ERROR)
                                    break
                                }
                            }
                        }
                        
                        if (uploadSuccess) {
                            snackbarManager.showSuccess("Item created successfully!")
                        }
                        
                        _state.update {
                            AddItemState(
                                publishLimit = it.publishLimit,
                                categories = it.categories
                            )
                        }
                    }
                    is Result.Error -> {
                        Log.e("add_viewmodel", createResult.toString())
                        snackbarManager.showSnackbar("Failed to create item. Please try again.", type = SnackbarType.ERROR)
                    }
                }
                
            } catch (e: Exception) {
                Log.e("add_viewmodel", e.toString())
                snackbarManager.showSnackbar("An unexpected error occurred. Please try again.", type = SnackbarType.ERROR)
            } finally {
                _state.update { it.copy(isSubmitting = false) }
            }
        }
    }
}