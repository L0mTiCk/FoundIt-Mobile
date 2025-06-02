package com.l0mtick.founditmobile.main.presentation.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun MessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.send_message_placeholder),
                    style = Theme.typography.body,
                    color = Theme.colors.onSurfaceVariant
                )
            },
            enabled = enabled,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.weight(1f),
            textStyle = Theme.typography.body,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Theme.colors.brand,
                cursorColor = Theme.colors.brand,
                selectionColors = TextSelectionColors(
                    handleColor = Theme.colors.brand,
                    backgroundColor = Theme.colors.brandMuted
                ),
                focusedTextColor = Theme.colors.onSurface,
                unfocusedTextColor = Theme.colors.onSurface
            )
        )
        
        IconButton(
            onClick = onSendClick,
            enabled = enabled && value.isNotBlank()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send message",
                tint = if (enabled && value.isNotBlank()) {
                    Theme.colors.brand
                } else {
                    Theme.colors.onSurfaceVariant
                }
            )
        }
    }
}