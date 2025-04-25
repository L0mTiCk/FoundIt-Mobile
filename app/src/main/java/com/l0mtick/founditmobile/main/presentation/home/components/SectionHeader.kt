package com.l0mtick.founditmobile.main.presentation.home.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun SectionHeader(
    @StringRes header: Int,
    @StringRes description: Int?,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(header),
            style = Theme.typography.title,
            color = Theme.colors.onSurface
        )
        if (description != null) {
            Text(
                text = stringResource(description),
                style = Theme.typography.description,
                color = Theme.colors.onSurfaceVariant
            )
        }
    }
}