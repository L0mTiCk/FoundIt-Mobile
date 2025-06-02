package com.l0mtick.founditmobile.main.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage
import com.l0mtick.founditmobile.common.presentation.components.defaultPlaceholder
import com.l0mtick.founditmobile.main.domain.model.Category
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun CategoryGrid(
    categories: List<Category>,
    modifier: Modifier = Modifier,
    onCategoryClick: (Long) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        if (categories.isEmpty()) {
            repeat(2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(2) {
                        CategoryCard(
                            modifier = Modifier.weight(1f),
                            isPlaceholderVisible = true,
                            onClick = {  }
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
        } else {
            categories.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { category ->
                        CategoryCard(
                            text = category.name,
                            imageUrl = category.pictureUrl,
                            modifier = Modifier
                                .weight(1f),
                            onClick = { onCategoryClick(category.id) }
                        )
                    }
                    if (rowItems.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}


@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    text: String = "",
    imageUrl: String = "",
    isPlaceholderVisible: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = Theme.colors.surface)
            .border(1.dp, color = Theme.colors.brandMuted, shape = RoundedCornerShape(16.dp))
            .clickable {
                onClick()
            }
            .padding(16.dp)
            .then(modifier)
    ) {
        Text(
            text = text,
            style = Theme.typography.body,
            color = Theme.colors.onSurface,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .defaultPlaceholder(visible = isPlaceholderVisible, width = 100.dp)
        )
        Spacer(Modifier.height(8.dp))
        PlaceholderImage(
            imageUrl = imageUrl,
            contentDescription = "Category image",
            modifier = Modifier
                .requiredSizeIn(minHeight = 100.dp)
                .aspectRatio(1f),
            isPlaceholderVisible = isPlaceholderVisible
        )
    }
}