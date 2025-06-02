package com.l0mtick.founditmobile.main.presentation.profile.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.PlaceholderImage
import com.l0mtick.founditmobile.common.presentation.components.createCropImageOptions
import com.l0mtick.founditmobile.common.presentation.components.defaultPlaceholder
import com.l0mtick.founditmobile.common.presentation.components.rememberImageCropperLauncher
import com.l0mtick.founditmobile.main.domain.model.User
import com.l0mtick.founditmobile.main.presentation.profile.ProfileAction
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun UserProfile(
    user: User?,
    modifier: Modifier = Modifier,
    onAction: (ProfileAction) -> Unit
) {

    var showImageActionsMenu by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberImageCropperLauncher(
        onImageSelected = { uri ->
            onAction(ProfileAction.ProfilePictureSelected(uri))
        },
        aspectRatioX = 1,
        aspectRatioY = 1
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .sizeIn(minWidth = 48.dp, minHeight = 48.dp, maxWidth = 80.dp, maxHeight = 80.dp)
        ) {
            PlaceholderImage(
                imageUrl = user?.profilePictureUrl,
                shape = CircleShape,
                contentDescription = "User Profile Picture",
                modifier = Modifier.fillMaxSize().aspectRatio(1f),
                isPlaceholderVisible = user == null,
                disableCache = true
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                IconButton(
                    onClick = { showImageActionsMenu = true },
                    modifier = Modifier
                        .background(
                            Theme.colors.onSurfaceVariant.copy(alpha = 0.7f),
                            CircleShape
                        )
                        .size(32.dp)

                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit or delete picture",
                        tint = Theme.colors.surface,
                        modifier = Modifier.size(18.dp)
                    )
                }

                DropdownMenu(
                    expanded = showImageActionsMenu,
                    onDismissRequest = { showImageActionsMenu = false },
                    containerColor = Theme.colors.surface
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(R.string.change_profile_picture),
                                style = Theme.typography.body,
                                color = Theme.colors.onSurface
                            )
                        },
                        onClick = {
                            imagePickerLauncher.launch(createCropImageOptions(
                                aspectRatioX = 1,
                                aspectRatioY = 1,
                                circleShape = true // Используем круглую рамку для фото профиля
                            ))
                            showImageActionsMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(R.string.delete_profile_picture),
                                style = Theme.typography.body,
                                color = Theme.colors.onSurface
                            )
                        },
                        onClick = {
                            onAction(ProfileAction.RemoveProfilePictureClicked)
                            showImageActionsMenu = false
                        }
                    )
                }
            }
        }
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
            User(1),
            onAction = {}
        )
    }
}