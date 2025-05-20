package com.l0mtick.founditmobile.main.presentation.profile.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun ProfileScreenRow(
    header: String,
    description: String? = null,
    @DrawableRes trailingResIcon: Int? = null,
    trailingIcon: ImageVector? = null,
    iconSize: Dp = 24.dp,
    isClickable: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .then(if (isClickable) Modifier.clickable { onClick() } else Modifier)
            .fillMaxWidth()
            .sizeIn(minHeight = 56.dp)
            .then(modifier)
    ) {
        Column {
            Text(
                text = header,
                style = Theme.typography.body
            )
            description?.let {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = it,
                    style = Theme.typography.description,
                    color = Theme.colors.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.weight(1f))
        trailingResIcon?.let {
            Icon(
                painter = painterResource(it),
                contentDescription = "",
                tint = Theme.colors.onSurface,
                modifier = Modifier.size(iconSize)
            )
        } ?: trailingIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = "",
                tint = Theme.colors.onSurface,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileRowPreview() {
    FoundItMobileTheme {
        ProfileScreenRow(
            header = "Favorites",
            description = "9 items",
            onClick = {}
        )
    }
}