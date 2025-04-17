package com.l0mtick.founditmobile.start.presentation.phoneverify

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.start.presentation.phoneverify.components.OpenTelegramButton
import com.l0mtick.founditmobile.start.presentation.phoneverify.components.OtpInputField
import com.l0mtick.founditmobile.start.presentation.phoneverify.components.PhoneConfirmationBottomSheet
import com.l0mtick.founditmobile.start.presentation.phoneverify.components.PhoneConfirmationText
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun PhoneVerificationRoot(
    viewModel: PhoneVerificationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PhoneVerificationScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun PhoneVerificationScreen(
    state: PhoneVerificationState,
    onAction: (PhoneVerificationAction) -> Unit,
) {
    var otpValue by remember { mutableStateOf("") }
    var isOtpFilled by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var showBottomSheet by remember { mutableStateOf(false) }
    var showTelegramButton by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(2000)
        showTelegramButton = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.phone_confirmation_title),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(12.dp))
        PhoneConfirmationText(
            onMoreInfoClick = { showBottomSheet = true }
        )
        Spacer(Modifier.height(32.dp))

        when (state) {
            PhoneVerificationState.PhoneEnter -> {
                Text("phone textfield")
                Spacer(Modifier.height(12.dp))
                AnimatedVisibility(
                    visible = showTelegramButton
                ) {
                    OpenTelegramButton(
                        onFailure = {
                            Toast.makeText(context, "Telegram app not found", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            PhoneVerificationState.CodeVerify -> {
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                }

                Text(
                    text = stringResource(R.string.enter_code_from_telegram),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(24.dp))
                OtpInputField(
                    modifier = Modifier.focusRequester(focusRequester),
                    otpText = otpValue,
                    onOtpModified = { value, isFilled ->
                        otpValue = value
                        isOtpFilled = isFilled
                        if (isOtpFilled) {
                            keyboardController?.hide()
                        }
                    },
                    shouldShowCursor = true,
                    shouldCursorBlink = true
                )
            }
        }
    }

    if (showBottomSheet) {
        PhoneConfirmationBottomSheet(
            onDismiss = { showBottomSheet = false },
            onContinue = { showBottomSheet = false }
        )
    }
}

@Preview(showBackground = true, locale = "RU")
@Preview(showBackground = true, locale = "EN")
@Composable
private fun Preview() {
    FoundItMobileTheme {
        PhoneVerificationScreen(
            state = PhoneVerificationState.PhoneEnter,
            onAction = {}
        )
    }
}