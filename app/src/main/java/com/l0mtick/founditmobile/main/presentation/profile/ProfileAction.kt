package com.l0mtick.founditmobile.main.presentation.profile

import android.net.Uri

sealed interface ProfileAction {
    data class ProfilePictureSelected(val uri: Uri) : ProfileAction
    data object RemoveProfilePictureClicked : ProfileAction
}