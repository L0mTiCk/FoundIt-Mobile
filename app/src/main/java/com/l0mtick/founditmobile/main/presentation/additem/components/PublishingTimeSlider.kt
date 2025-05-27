package com.l0mtick.founditmobile.main.presentation.additem.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.ui.theme.Theme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishingTimeSlider(
    userLimit: Float,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = stringResource(android.R.string.ok), color = Theme.colors.brand)
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.publication_duration_info_title),
                    style = Theme.typography.title,
                    color = Theme.colors.onSurface
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.publication_duration_info_text),
                    style = Theme.typography.body
                )
            },
            containerColor = Theme.colors.surface,
            textContentColor = Theme.colors.onSurface
        )
    }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val days = value.roundToInt()
            Text(
                text = pluralStringResource(R.plurals.days_count, days, days),
                style = Theme.typography.body,
                color = Theme.colors.onSurface
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "Max: ${userLimit.roundToInt()}",
                style = Theme.typography.body,
                color = Theme.colors.onSurface
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = {
                    showDialog = true
                }
            ) {
                Icon(Icons.Outlined.Info, "Info", tint = Theme.colors.onSurface)
            }
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 1f..userLimit,
            steps = (userLimit - 1).roundToInt() - 1,
            colors = SliderDefaults.colors(
                thumbColor = Theme.colors.brand,
                activeTrackColor = Theme.colors.brand,
                activeTickColor = Color.Transparent,
                inactiveTrackColor = Theme.colors.brandMuted,
                inactiveTickColor = Theme.colors.onBrand
            ),
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = interactionSource,
                    thumbSize = DpSize(5.dp, 22.dp),
                    colors = SliderDefaults.colors(thumbColor = Theme.colors.brand)
                )
            }
        )
    }
}