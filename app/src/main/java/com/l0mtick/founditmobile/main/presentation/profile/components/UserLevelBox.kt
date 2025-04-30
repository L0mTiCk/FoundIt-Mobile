package com.l0mtick.founditmobile.main.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun UserLevelBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = Theme.colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(3f)
            ) {
                Text(
                    text = "Level 1: Novice",
                    style = Theme.typography.title
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "You're on your way to becoming a pro. Publish more items to unlock new levels",
                    style = Theme.typography.description,
                    color = Theme.colors.onSurfaceVariant,
                    minLines = 2
                )
            }
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "",
                modifier = Modifier.weight(1f).requiredSize(64.dp)
            )
        }
    }
}