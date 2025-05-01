package com.l0mtick.founditmobile.main.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage

@Composable
fun ItemImagesPager(
    imageUrls: List<String>,
    modifier: Modifier = Modifier
) {
    val pagerState = remember { PagerState(pageCount = { imageUrls.size }) }

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
                modifier = Modifier.sizeIn(minHeight = 150.dp, maxHeight = 200.dp)
            )
        }
    }
}