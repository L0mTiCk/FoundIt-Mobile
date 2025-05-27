package com.l0mtick.founditmobile.main.presentation.profile.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
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
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage
import com.l0mtick.founditmobile.common.presentation.components.defaultPlaceholder
import com.l0mtick.founditmobile.main.domain.model.User
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun UserProfile(
    user: User?,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        PlaceholderImage(
            imageUrl = user?.profilePictureUrl,
            shape = CircleShape,
            contentDescription = "User Profile Picture",
            modifier = Modifier
                .sizeIn(maxWidth = 80.dp)
                .weight(1f)
                .aspectRatio(1f),
            isPlaceholderVisible = user == null
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(3f)) {
            LookaheadScope {
                Text(
                    text = user?.username ?: "",
                    style = Theme.typography.headline,
                    color = Theme.colors.onSurface,
                    modifier = Modifier
                        .defaultPlaceholder(visible = user == null, width = 100.dp)
                        .animateBounds(this)
                )
            }
            Spacer(Modifier.height(4.dp))
            LookaheadScope {
                Text(
                    text = user?.email ?: "",
                    style = Theme.typography.body,
                    color = Theme.colors.onSurfaceVariant,
                    modifier = Modifier
                        .defaultPlaceholder(visible = user == null, width = 150.dp)
                        .animateBounds(this)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserProfilePreview() {
    FoundItMobileTheme {
        UserProfile(
            User(1)
        )
    }
}