package com.l0mtick.founditmobile.main.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage
import com.l0mtick.founditmobile.common.presentation.components.PrimaryButton
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun UserHeaderCard(
    isGuest: Boolean,
    onMoveToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    username: String = "L0mTiCk",
    profilePictureUrl: String? = null
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(R.string.user_greeting),
                style = Theme.typography.description,
                color = Theme.colors.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (isGuest) stringResource(R.string.guest) else username,
                style = Theme.typography.body,
                color = Theme.colors.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(Modifier.weight(1f))
        if (isGuest) {
            PrimaryButton(
                text = stringResource(R.string.log_in),
                onClick = onMoveToLogin
            )
            Spacer(Modifier.width(12.dp))
        }
        PlaceholderImage(
            imageUrl = profilePictureUrl,
            contentDescription = "Profile picture",
            modifier = Modifier.requiredSize(44.dp),
            shape = CircleShape,
            disableCache = true
        )
    }
}