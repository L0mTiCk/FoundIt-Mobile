package com.l0mtick.founditmobile.common.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarData
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarManager
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarType
import com.l0mtick.founditmobile.ui.theme.Theme
import kotlinx.coroutines.delay

@Composable
fun CustomSnackbar(
    snackbarManager: SnackbarManager,
    modifier: Modifier = Modifier
) {
    val snackbarState by snackbarManager.snackbarState.collectAsState()
    val context = LocalContext.current

    AnimatedVisibility(
        visible = snackbarState != null,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(300)
        ),
        modifier = modifier
    ) {
        snackbarState?.let { data ->
            LaunchedEffect(data) {
                delay(data.duration)
                snackbarManager.hideSnackbar()
            }

            SnackbarContent(
                data = data,
                context = context
            )
        }
    }
}

@Composable
private fun SnackbarContent(
    data: SnackbarData,
    context: android.content.Context
) {
    val (backgroundColor, iconColor, icon) = when (data.type) {
        SnackbarType.SUCCESS -> Triple(
            Theme.colors.brand,
            Color.White,
            Icons.Default.CheckCircle
        )
        SnackbarType.ERROR -> Triple(
            Color(0xFFE53E3E),
            Color.White,
            Icons.Default.Warning
        )
        SnackbarType.WARNING -> Triple(
            Color(0xFFED8936),
            Color.White,
            Icons.Default.Warning
        )
        SnackbarType.INFO -> Triple(
            Theme.colors.onSurfaceVariant,
            Color.White,
            Icons.Default.Info
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = data.message.asString(context),
                style = Theme.typography.body,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
        }
    }
}