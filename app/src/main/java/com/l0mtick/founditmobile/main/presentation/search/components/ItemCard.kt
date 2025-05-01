package com.l0mtick.founditmobile.main.presentation.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage
import com.l0mtick.founditmobile.main.presentation.util.formatTimeAgo
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun BigItemCard(
    title: String,
    description: String,
    postedTimestamp: Long,
    distance: Int,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .then(modifier)
    ) {
        PlaceholderImage(
            imageUrl = imageUrl,
            contentDescription = "Item image",
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.sizeIn(minHeight = 150.dp, maxHeight = 200.dp)
        )
        Spacer(Modifier.height(18.dp))
        Text(
            text = title,
            style = Theme.typography.title
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = description,
            style = Theme.typography.body,
            color = Theme.colors.onSurfaceVariant
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
                        Text("$distance m")
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
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Theme.colors.brand),
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = "Details",
                    style = Theme.typography.body,
                    color = Theme.colors.onBrand
                )
            }
        }
    }
}

@Composable
fun SmallItemCard(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
private fun BigItemCardPreview() {
    FoundItMobileTheme {
        BigItemCard(
            title = "Black backpack",
            description = "At the bus stop at 4th and king",
            postedTimestamp = 1746034187000L,
            distance = 100,
            onClick = {}
        )
    }
}