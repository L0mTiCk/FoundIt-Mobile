package com.l0mtick.founditmobile.main.presentation.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage
import com.l0mtick.founditmobile.common.presentation.components.PrimaryButton
import com.l0mtick.founditmobile.main.domain.model.Category
import com.l0mtick.founditmobile.main.domain.model.LostItem
import com.l0mtick.founditmobile.main.domain.model.LostItemStatus
import com.l0mtick.founditmobile.main.presentation.util.formatTimeAgo
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapModalBottomSheet(
    item: LostItem,
    onDismissRequest: () -> Unit,
    onViewDetailsClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = Theme.colors.surface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = item.title,
                style = Theme.typography.title,
                color = Theme.colors.onSurface,
            )
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.padding(horizontal = 8.dp)
            ) {
                PlaceholderImage(
                    imageUrl = item.photoUrls.firstOrNull(),
                    contentDescription = "Item image",
                    modifier = Modifier
                        .size(
                            width = 100.dp,
                            height = 120.dp
                        )
                        .weight(1f)
                )
                Spacer(Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(2f).sizeIn(minHeight = 120.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = item.description ?: "No description",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = Theme.typography.body,
                        color = Theme.colors.onSurface,
                    )
//                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = formatTimeAgo(item.createdAt),
                        style = Theme.typography.small,
                        color = Theme.colors.onSurfaceVariant
                    )
//                    Spacer(Modifier.height(4.dp))
                    PrimaryButton(
                        text = "View card",
                        onClick = { onViewDetailsClick(item.id) },
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MapModalBottomSheetPreview() {
    FoundItMobileTheme {
        val item = LostItem(
            id = 1,
            userId = 1,
            latitude = 11.0,
            longitude = 11.0,
            title = "Black backpack",
            description = "Some kind of item description that should present in real items",
            isFound = false,
            createdAt = 1746453451000L,
            expiresAt = 1746712651000L,
            photoUrls = emptyList(),
            isModerated = true,
            status = LostItemStatus.ACTIVE,
            categories = listOf(
                Category(
                    id = 1,
                    name = "Wallets",
                    pictureUrl = ""
                )
            )
        )
        MapModalBottomSheet(
            item = item,
            onDismissRequest = {},
            onViewDetailsClick = {}
        )
    }
}