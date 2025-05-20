package com.l0mtick.founditmobile.main.presentation.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.PrimaryButton
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun AboutDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Theme.colors.background
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.about_dialog_title),
                    style = Theme.typography.title
                )
                Spacer(Modifier.height(14.dp))
                Text(
                    text = stringResource(R.string.about_dialog_description),
                    style = Theme.typography.body
                )
                Spacer(Modifier.height(12.dp))
                PrimaryButton(
                    text = stringResource(R.string.ok),
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutDialogPreview() {
    FoundItMobileTheme {
        AboutDialog(
            onDismiss = {}
        )
    }
}