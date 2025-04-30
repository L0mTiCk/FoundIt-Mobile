package com.l0mtick.founditmobile.main.presentation.search.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.main.domain.model.Category
import com.l0mtick.founditmobile.ui.theme.Theme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CategoriesFilterRow(
    categories: List<Category>,
    selectedCategories: Set<Long>,
    onCategoryClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .then(modifier)
    ) {
        categories.forEach {
            val selected = it.id in selectedCategories
            val trailingAlpha = animateFloatAsState(if (selected) 1f else 0f)
            FilterChip(
                onClick = {
                    onCategoryClick(it.id)
                },
                label = {
                    Text(it.name, maxLines = 1)
                },
                selected = selected,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Theme.colors.brand.copy(alpha = .2f),
                    selectedLabelColor = Theme.colors.onSurfaceVariant,
                    containerColor = Theme.colors.onSurfaceVariant.copy(alpha = .2f),
                    labelColor = Theme.colors.onSurface
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(horizontal = 4.dp).animateContentSize(),
                trailingIcon = {
                    if (selected) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove category",
                            tint = Theme.colors.onSurfaceVariant,
                            modifier = Modifier.alpha(trailingAlpha.value)
                        )
                    }
                }
            )
        }
    }
}