package com.l0mtick.founditmobile.main.presentation.components

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun FloatingModeSwitchButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button (
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Theme.colors.brandMuted,
            contentColor = Theme.colors.brand
        ),
        modifier = Modifier
            .sizeIn(minWidth = 128.dp)
            .then(modifier)
    ) {
        Text(
            text = text,
            style = Theme.typography.body,
            color = Theme.colors.brand
        )
    }
}