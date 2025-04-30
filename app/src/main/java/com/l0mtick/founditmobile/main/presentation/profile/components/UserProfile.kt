package com.l0mtick.founditmobile.main.presentation.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun UserProfile(
    userPictureUrl: String,
    username: String,
    email: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        PlaceholderImage(
            imageUrl = userPictureUrl,
            shape = CircleShape,
            contentDescription = "User Profile Picture",
            modifier = Modifier
                .sizeIn(maxWidth = 80.dp)
                .weight(1f)
                .aspectRatio(1f)
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(2.5f)) {
            Text(
                text = username,
                style = Theme.typography.headline
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = email,
                style = Theme.typography.body,
                color = Theme.colors.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserProfilePreview() {
    FoundItMobileTheme {
        UserProfile(
            "", "Olivia", "oliviamail@gmail.com"
        )
    }
}