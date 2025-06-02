package com.l0mtick.founditmobile.start.presentation.phoneverify.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.PrimaryButton
import com.l0mtick.founditmobile.ui.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneConfirmationBottomSheet(
    onDismiss: () -> Unit,
    onContinue: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Theme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.phone_confirmation_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Theme.colors.onSurface,
            )
            Text(
                text = stringResource(R.string.phone_confirmation_description),
                style = MaterialTheme.typography.bodyMedium,
                color = Theme.colors.onSurface,
            )
            Text(
                text = stringResource(R.string.phone_confirmation_note),
                style = MaterialTheme.typography.bodySmall,
                color = Theme.colors.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = stringResource(R.string.phone_confirmation_continue),
                modifier = Modifier.fillMaxWidth(),
                onClick = onContinue
            )
        }
    }
}