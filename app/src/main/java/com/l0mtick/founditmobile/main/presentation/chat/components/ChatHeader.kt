package com.l0mtick.founditmobile.main.presentation.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage
import com.l0mtick.founditmobile.main.presentation.components.ConfirmationDialog
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun ChatHeader(
    username: String,
    logoUrl: String?,
    onBackPressed: () -> Unit,
    onDeleteChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDropdownOpened by remember { mutableStateOf(false) }
    var isConfirmShown by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackPressed
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Exit chat",
                tint = Theme.colors.onSurface
            )
        }
        Spacer(Modifier.width(8.dp))
        PlaceholderImage(
            imageUrl = logoUrl,
            contentDescription = "User logo",
            shape = CircleShape,
            modifier = Modifier.size(48.dp),
            disableCache = true
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = username,
            style = Theme.typography.body,
            fontWeight = FontWeight.Medium,
            color = Theme.colors.onSurface
        )
        Spacer(Modifier.weight(1f))
        Spacer(Modifier.width(12.dp))
        Box {
            IconButton(onClick = {
                isDropdownOpened = true
            }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "",
                    tint = Theme.colors.onSurface
                )
            }
            DropdownMenu(
                expanded = isDropdownOpened,
                onDismissRequest = {
                    isDropdownOpened = false
                },
                containerColor = Theme.colors.surface
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.delete_chat_dropdown_item),
                            style = Theme.typography.body,
                            color = Theme.colors.onSurface
                        )
                    },
                    onClick = {
                        isConfirmShown = true
                    }
                )
            }
        }
    }
    if (isConfirmShown) {
        ConfirmationDialog(
            title = stringResource(R.string.delete_chat_confirm_header),
            message = stringResource(R.string.delete_chat_confirm_description),
            confirmButtonText = stringResource(R.string.delete),
            dismissButtonText = stringResource(R.string.cancel),
            onConfirm = {
                onDeleteChat()
                isConfirmShown = false
                isDropdownOpened = false
            },
            onDismiss = {
                isConfirmShown = false
                isDropdownOpened = false
            }
        )
    }
}