package com.l0mtick.founditmobile.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun ItemImagesPager(
    imageUrls: List<String>,
    modifier: Modifier = Modifier
) {
    val pagerState = remember(imageUrls.size) { PagerState(pageCount = { imageUrls.size }) }

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 8.dp
        ) { page ->
            PlaceholderImage(
                imageUrl = imageUrls[page],
                contentDescription = "Item image",
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.sizeIn(minHeight = 150.dp, maxHeight = 200.dp).align(Alignment.Center),
                contentScale = ContentScale.FillBounds
            )
        }
        Text(
            text = "${pagerState.currentPage + 1}/${imageUrls.size}",
            style = Theme.typography.body,
            color = Theme.colors.surface,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .background(
                    Theme.colors.onSurface.copy(alpha = .4f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .align(Alignment.TopEnd)
        )
    }
}