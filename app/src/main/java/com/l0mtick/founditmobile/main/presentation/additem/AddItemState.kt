package com.l0mtick.founditmobile.main.presentation.additem

import android.net.Uri
import com.l0mtick.founditmobile.common.presentation.util.TextFieldState
import com.l0mtick.founditmobile.main.domain.model.Category

data class AddItemState(
    val title: TextFieldState = TextFieldState(),
    val description: TextFieldState = TextFieldState(),
    val selectedCategory: Category? = null,
    val categories: List<Category> = emptyList(),
    val selectedPhotos: List<Uri> = emptyList(),
    val isCategoryDropdownExpanded: Boolean = false
)