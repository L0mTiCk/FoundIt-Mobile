package com.l0mtick.founditmobile.common.presentation.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.VisualTransformation
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun OutlinedAppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    isError: Boolean = false,
    errorText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onFocusChanged: ((FocusState) -> Unit)? = null,
    singleLine: Boolean = true,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .then(
                if (onFocusChanged != null) Modifier.onFocusChanged { onFocusChanged(it) }
                else Modifier
            ),
        label = label?.let { { Text(it) } },
        isError = isError,
        supportingText = {
            if (isError && !errorText.isNullOrBlank()) {
                Text(errorText)
            }
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Theme.colors.onSurface,
            focusedBorderColor = Theme.colors.brand,
            focusedLabelColor = Theme.colors.brand,
            cursorColor = Theme.colors.brand,
            selectionColors = TextSelectionColors(
                handleColor = Theme.colors.brand,
                backgroundColor = Theme.colors.brandMuted
            )
        )
    )
}