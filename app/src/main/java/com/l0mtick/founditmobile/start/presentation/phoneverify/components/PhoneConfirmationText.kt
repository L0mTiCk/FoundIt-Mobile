package com.l0mtick.founditmobile.start.presentation.phoneverify.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.l0mtick.founditmobile.R

@Composable
fun PhoneConfirmationText(
    onMoreInfoClick: () -> Unit
) {
    val annotatedText = buildAnnotatedString {
        append(stringResource(R.string.phone_confirmation_short))
        append(" ")

        pushStringAnnotation(
            tag = "MORE_INFO",
            annotation = "more_info"
        )
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(stringResource(R.string.more_info))
        }
        pop()
    }

    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        text = annotatedText,
        style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures { offsetPosition ->
                    layoutResult?.let { result ->
                        val position = result.getOffsetForPosition(offsetPosition)
                        annotatedText.getStringAnnotations(
                            tag = "MORE_INFO",
                            start = position,
                            end = position
                        ).firstOrNull()?.let {
                            onMoreInfoClick()
                        }
                    }
                }
            },
        onTextLayout = { layoutResult = it }
    )
}