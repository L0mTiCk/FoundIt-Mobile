package com.l0mtick.founditmobile.main.presentation.additem

import android.net.Uri
import com.l0mtick.founditmobile.common.presentation.util.TextFieldState
import com.l0mtick.founditmobile.main.domain.model.Category

data class AddItemState(
    val isSubmitting: Boolean = false,
    val title: TextFieldState = TextFieldState(),
    val description: TextFieldState = TextFieldState(),
    val selectedCategory: Category? = null,
    val publishTime: Float = 1f,
    val publishLimit: Float = 3f,
    val categories: List<Category> = emptyList(),
    val selectedPhotos: List<Uri> = emptyList(),
)