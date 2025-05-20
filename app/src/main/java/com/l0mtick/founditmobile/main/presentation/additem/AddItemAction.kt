package com.l0mtick.founditmobile.main.presentation.additem

import android.net.Uri
import com.l0mtick.founditmobile.main.domain.model.Category

sealed interface AddItemAction {
    data class UpdateTitle(val title: String) : AddItemAction
    data class UpdateDescription(val description: String) : AddItemAction
    data class SelectCategory(val category: Category) : AddItemAction
    data class AddPhoto(val uri: Uri) : AddItemAction
    data class RemovePhoto(val uri: Uri) : AddItemAction
    data object ToggleCategoryDropdown : AddItemAction
    data object CenterOnUserLocation : AddItemAction
    data object SubmitItem : AddItemAction
}