package com.l0mtick.founditmobile.common.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoWifiBottomSheet(
    modifier: Modifier = Modifier
) {
    val bottomSheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { newState ->
                newState != SheetValue.Hidden
            })
    ModalBottomSheet(
        modifier = Modifier
            .then(modifier),
        sheetState = bottomSheetState,
        onDismissRequest = {},
        dragHandle = {},
        containerColor = Theme.colors.surface,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.45f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(64.dp),
                painter = painterResource(R.drawable.no_signal),
                tint = MaterialTheme.colorScheme.error,
                contentDescription = "No wifi icon"
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.no_internet_header),
                style = Theme.typography.headline
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.no_internet_description),
                style = Theme.typography.body,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoWifiPreview() {
    FoundItMobileTheme {
        NoWifiBottomSheet()
    }
}