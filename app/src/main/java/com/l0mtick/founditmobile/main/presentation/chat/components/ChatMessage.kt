package com.l0mtick.founditmobile.main.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun ChatMessage(
    message: String,
    sentDate: String,
    isFromMe: Boolean,
    username: String?,
    modifier: Modifier = Modifier,
    logoUrl: String? = null
) {
    val messageForm =
        if (isFromMe) RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp)
        else RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isFromMe) {
                Text(
                    text = sentDate,
                    style = Theme.typography.small,
                    color = Theme.colors.onSurfaceVariant
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "You",
                    style = Theme.typography.small,
                    color = Theme.colors.onSurfaceVariant
                )
            } else {
                PlaceholderImage(
                    imageUrl = logoUrl,
                    contentDescription = "User logo",
                    shape = CircleShape,
                    modifier = Modifier.size(42.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = username ?: "placeholder",
                    style = Theme.typography.small,
                    color = Theme.colors.onSurfaceVariant
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = sentDate,
                    style = Theme.typography.small,
                    color = Theme.colors.onSurfaceVariant
                )
            }
        }
        Box(
            modifier = Modifier
                .then(if (!isFromMe) Modifier.padding(start = 52.dp).offset(y = (-8).dp) else Modifier.padding(top = 8.dp))
                .clip(messageForm)
                .background(Theme.colors.surface)
                .padding(12.dp)
        ) {
            Text(
                text = message,
                style = Theme.typography.body,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatMessagePreview() {
    FoundItMobileTheme {
        ChatMessage(
            "Hello and welcome to zootopia Hello and welcome to zootopia Hello and welcome to zootopia Hello and welcome to zootopia",
            "9:00 pm",
            isFromMe = true,
            username = null
        )
    }
}