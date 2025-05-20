package com.l0mtick.founditmobile.main.presentation.additem

import android.net.Uri
import com.l0mtick.founditmobile.main.domain.model.Category

data class AddItemState(
    val title: String = "",
    val description: String = "",
    val selectedCategory: Category? = null,
    val categories: List<Category> = emptyList(),
    val selectedPhotos: List<Uri> = emptyList(),
    val userLatitude: Double = 0.0,
    val userLongitude: Double = 0.0,
    val isCategoryDropdownExpanded: Boolean = false
)