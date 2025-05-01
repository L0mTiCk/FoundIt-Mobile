package com.l0mtick.founditmobile.main.presentation.lostitemdetails.components

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage
import com.l0mtick.founditmobile.main.domain.model.User
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun ItemAuthorCard(
    user: User,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlaceholderImage(
            imageUrl = user.profilePictureUrl,
            contentDescription = "User logo",
            shape = CircleShape,
            modifier = Modifier.requiredSize(56.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                text = user.username,
                style = Theme.typography.body
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = context.resources.getQuantityString(R.plurals.items_count, user.levelItemsCount, user.levelItemsCount),
                style = Theme.typography.description,
                color = Theme.colors.onSurfaceVariant
            )
        }
    }
}