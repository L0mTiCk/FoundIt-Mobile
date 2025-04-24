package com.l0mtick.founditmobile.main.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage
import com.l0mtick.founditmobile.common.presentation.components.defaultPlaceholder
import com.l0mtick.founditmobile.main.domain.model.User
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun TopLevelUsersRow(
    users: List<User>,
    modifier: Modifier = Modifier,
    onUserCardClick: (Long) -> Unit
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.width(24.dp))
        if (users.isEmpty()) {
            repeat(3) {
                UserLevelCard(
                    isPlaceholderVisible = true,
                    onCardClick = {}
                )
            }
        } else {
            users.forEach { user ->
                UserLevelCard(
                    profilePictureUrl = user.profilePictureUrl,
                    username = user.username,
                    level = user.level,
                    numberOfItemsFound = user.levelItemsCount,
                    modifier = Modifier.sizeIn(maxWidth = 170.dp, maxHeight = 300.dp),
                    onCardClick = { onUserCardClick(user.id) }
                )
            }
        }
        Spacer(Modifier.width(24.dp))
    }
}

@Composable
private fun UserLevelCard(
    modifier: Modifier = Modifier,
    profilePictureUrl: String? = null,
    username: String = "",
    level: Int = 0,
    numberOfItemsFound: Int = 0,
    onCardClick: () -> Unit,
    isPlaceholderVisible: Boolean = false
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = Theme.colors.surface)
            .border(1.dp, color = Theme.colors.secondary, shape = RoundedCornerShape(16.dp))
            .clickable { onCardClick() }
            .padding(16.dp)
            .then(modifier)
    ) {
        PlaceholderImage(
            imageUrl = profilePictureUrl,
            contentDescription = "User profile picture",
            modifier = Modifier
                .requiredSizeIn(minHeight = 100.dp, minWidth = 170.dp)
                .aspectRatio(1f),
            isPlaceholderVisible = isPlaceholderVisible
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = username,
            style = Theme.typography.body,
            color = Theme.colors.onSurface,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .defaultPlaceholder(visible = isPlaceholderVisible, width = 100.dp)
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .defaultPlaceholder(visible = isPlaceholderVisible)
                .background(color = Theme.colors.brand.copy(.2f))
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.user_level_with_count, level, numberOfItemsFound),
                style = Theme.typography.small,
                color = Theme.colors.brand,
            )
        }
    }
}