package com.l0mtick.founditmobile.main.presentation.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage
import com.l0mtick.founditmobile.common.presentation.components.PrimaryButton
import com.l0mtick.founditmobile.main.domain.model.LostItemStatus
import com.l0mtick.founditmobile.main.presentation.util.formatTimeAgo
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme
import kotlin.math.roundToInt

@Composable
fun BigItemCard(
    id: Int,
    title: String,
    description: String,
    postedTimestamp: Long,
    distance: Float?,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    onClick: (Int) -> Unit
) {
    val distanceText = distance?.let {
        if (distance < 1000) {
            "${distance.roundToInt()} m"
        } else {
            "${(distance / 1000).roundToInt()} km"
        }
    } ?: "..."
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick(id) }
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .then(modifier)
    ) {
        PlaceholderImage(
            imageUrl = imageUrl,
            contentDescription = "Item image",
            shape = RoundedCornerShape(12.dp),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(4f / 3f)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(18.dp))
        Text(
            text = title,
            style = Theme.typography.title,
            color = Theme.colors.onSurface
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = description,
            style = Theme.typography.body,
            color = Theme.colors.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
        ) {
            Column {
                Text(
                    text = formatTimeAgo(postedTimestamp),
                    style = Theme.typography.description,
                    color = Theme.colors.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                FilterChip(
                    onClick = {},
                    label = {
                        Text(
                            text = distanceText,
                            style = Theme.typography.description
                        )
                    },
                    selected = true,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Theme.colors.brand.copy(alpha = .2f),
                        selectedLabelColor = Theme.colors.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
            Spacer(Modifier.weight(1f))
            PrimaryButton(
                text = stringResource(R.string.more_info),
                onClick = { onClick(id) },
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BigItemCardPreview() {
    FoundItMobileTheme {
        BigItemCard(
            id = -1,
            title = "Black backpack",
            description = "At the bus stop at 4th and king",
            postedTimestamp = 1746034187000L,
            distance = 100F,
            onClick = {}
        )
    }
}

@Composable
fun CompactItemCard(
    id: Int,
    title: String,
    description: String,
    postedTimestamp: Long,
    modifier: Modifier = Modifier,
    status: LostItemStatus = LostItemStatus.ACTIVE,
    imageUrl: String? = null,
    isUserCreated: Boolean = true,
    onClick: (Int) -> Unit,
    onActionClick: (Int) -> Unit,
    onMarkAsReturned: (Int) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick(id) }
            .padding(12.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image
        PlaceholderImage(
            imageUrl = imageUrl,
            contentDescription = "Item image",
            shape = RoundedCornerShape(8.dp),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .sizeIn(maxWidth = 100.dp)
                .aspectRatio(4f / 3f)
        )
        
        Spacer(Modifier.width(12.dp))
        
        // Content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = Theme.typography.body,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Theme.colors.onSurface
            )
            
            Spacer(Modifier.height(4.dp))
            
            Text(
                text = description,
                style = Theme.typography.description,
                color = Theme.colors.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(Modifier.height(4.dp))
            
            Text(
                text = formatTimeAgo(postedTimestamp),
                style = Theme.typography.description,
                color = Theme.colors.onSurfaceVariant
            )

            Spacer(Modifier.height(4.dp))

            StatusChip(status = status)
        }
        if (isUserCreated) {
            IconButton(
                onClick = { onMarkAsReturned(id) }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Mark as returned",
                    tint = Theme.colors.brand
                )
            }
        }
        // Action button
        IconButton(
            onClick = { onActionClick(id) }
        ) {
            Icon(
                imageVector = if (isUserCreated) Icons.Filled.Delete else Icons.Filled.Favorite,
                contentDescription = if (isUserCreated) "Delete item" else "Remove from favorites",
                tint = Theme.colors.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun CompactItemCardPreview() {
    FoundItMobileTheme {
        CompactItemCard(
            id = 1,
            title = "Lost iPhone 13 Pro",
            description = "I lost my iPhone 13 Pro in the park yesterday. It has a black case and a cracked screen.",
            postedTimestamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000,
            isUserCreated = true,
            onClick = {},
            onActionClick = {},
        )
    }
}

@Composable
fun StatusChip(
    status: LostItemStatus,
    modifier: Modifier = Modifier
) {
    val backgroundColor: Color
    val statusText: String
    val textColor: Color = Color.White

    when (status) {
        LostItemStatus.PENDING -> {
            backgroundColor = Theme.colors.statusPending
            statusText = stringResource(id = R.string.item_status_pending)
        }
        LostItemStatus.ACTIVE -> {
            backgroundColor = Theme.colors.brand
            statusText = stringResource(id = R.string.item_status_active)
        }
        LostItemStatus.EXPIRED -> {
            backgroundColor = Theme.colors.statusExpired
            statusText = stringResource(id = R.string.item_status_expired)
        }
        LostItemStatus.FOUND -> {
            backgroundColor = Theme.colors.statusFound
            statusText = stringResource(id = R.string.item_status_found)
        }
    }


    Text(
        text = statusText,
        color = textColor,
        style = Theme.typography.description,
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 3.dp)
    )
}