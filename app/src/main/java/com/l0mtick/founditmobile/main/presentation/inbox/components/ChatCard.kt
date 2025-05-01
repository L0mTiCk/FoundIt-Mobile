package com.l0mtick.founditmobile.main.presentation.inbox.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun ChatCard(
    itemImageUrl: String,
    userImageUrl: String?,
    username: String,
    itemHeader: String,
    lastMessage: String,
    messageDate: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .heightIn(max = 92.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemWithOwnerImage(
            itemUrl = itemImageUrl,
            userUrl = userImageUrl
        )
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row {
                Text(
                    text = username,
                    style = Theme.typography.body,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = messageDate,
                    style = Theme.typography.description,
                    color = Theme.colors.onSurfaceVariant
                )
            }
            Text(
                text = itemHeader,
                style = Theme.typography.body,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = lastMessage,
                style = Theme.typography.description,
                color = Theme.colors.onSurfaceVariant,
                overflow = TextOverflow.StartEllipsis,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatCardPreview() {
    FoundItMobileTheme {
        ChatCard(
            "",
            "",
            "ammmm_v",
            "Black backpack at corner shop",
            "So will you take it take it take it take it it take it it take it",
            "15.05.2025",
            {}
        )
    }
}