package com.l0mtick.founditmobile.common.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = Theme.typography.body,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Theme.colors.brand,
        contentColor = Theme.colors.onBrand
    )
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = buttonColors
    ) {
        Text(
            text = text,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun LoadingPrimaryButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = Theme.typography.body,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Theme.colors.brand,
        contentColor = Theme.colors.onBrand
    )
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = buttonColors
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                style = textStyle,
                modifier = Modifier.align(Alignment.Center),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            FadeVisibility(
                visible = isLoading,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                CircularProgressIndicator(
                    color = buttonColors.contentColor,
                    modifier = Modifier
                        .size(24.dp),
                    strokeWidth = 2.dp
                )

            }
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = Theme.typography.body,
    buttonColors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = Theme.colors.brand
    )
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = buttonColors,
        border = ButtonDefaults.outlinedButtonBorder()
    ) {
        Text(
            text = text,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun LoadingSecondaryButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = Theme.typography.body,
    buttonColors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = Theme.colors.brand,
        contentColor = Theme.colors.onBrand
    )
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = buttonColors
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                style = textStyle,
                modifier = Modifier.align(Alignment.Center),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            FadeVisibility(
                visible = isLoading,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                CircularProgressIndicator(
                    color = buttonColors.contentColor,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd),
                    strokeWidth = 2.dp
                )

            }
        }
    }
}