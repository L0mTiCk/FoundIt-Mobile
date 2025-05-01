package com.l0mtick.founditmobile.main.presentation.inbox.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun ItemWithOwnerImage(
    itemUrl: String,
    userUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .size(width = 100.dp, height = 75.dp)
            .then(modifier)
    ) {
        PlaceholderImage(
            imageUrl = itemUrl,
            contentDescription = "Item image",
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Theme.colors.background)
                .align(Alignment.BottomEnd)
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            PlaceholderImage(
                imageUrl = userUrl,
                contentDescription = "User logo",
                modifier = Modifier
                    .size(40.dp),
                shape = CircleShape
            )
        }
    }
}