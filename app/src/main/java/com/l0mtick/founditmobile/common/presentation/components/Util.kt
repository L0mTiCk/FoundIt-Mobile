package com.l0mtick.founditmobile.common.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.placeholder
import com.eygraber.compose.placeholder.shimmer

fun Modifier.defaultPlaceholder(
    visible: Boolean,
    width: Dp? = null,
    height: Dp? = null,
    placeholderColor: Color = Color.LightGray,
    shimmerColor: Color = Color.White,
    placeholderModifier: Modifier = Modifier
): Modifier = this.then(
    if (visible) {
        Modifier
            .then(placeholderModifier)
            .placeholder(
                visible = true,
                color = placeholderColor,
                highlight = PlaceholderHighlight.shimmer(highlightColor = shimmerColor)
            )
            .then(if (width != null) Modifier.width(width) else Modifier)
            .then(if (height != null) Modifier.height(height) else Modifier)
    } else {
        Modifier
    }
)


