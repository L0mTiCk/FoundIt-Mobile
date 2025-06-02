package com.l0mtick.founditmobile.start.presentation.phoneverify

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.common.presentation.util.ObserveAsEvents
import com.l0mtick.founditmobile.start.presentation.phoneverify.components.OpenTelegramButton
import com.l0mtick.founditmobile.start.presentation.phoneverify.components.OtpInputField
import com.l0mtick.founditmobile.start.presentation.phoneverify.components.PhoneConfirmationBottomSheet
import com.l0mtick.founditmobile.start.presentation.phoneverify.components.PhoneConfirmationText
import com.l0mtick.founditmobile.start.presentation.phoneverify.components.PhoneTextFieldWithCountry
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme
import org.koin.androidx.compose.koinViewModel

@Composable
fun PhoneVerificationRoot(
    navController: NavController,
    viewModel: PhoneVerificationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val telegramUrl = "tg://resolve?domain=FoundIt_Verification_Bot"

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is PhoneVerificationEvent.OpenTelegramBot -> {
                val intent = Intent(Intent.ACTION_VIEW, telegramUrl.toUri())
                try {
                    context.startActivity(intent)
                    viewModel.onAction(PhoneVerificationAction.OnMoveToCode(event.fullPhoneNumber))
                } catch (e: Exception) {
                    Toast.makeText(context, "Telegram app not found", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            PhoneVerificationEvent.OnVerificationSuccess -> {
                navController.navigate(NavigationRoute.Start.Login)
            }
        }
    }

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
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.phone_confirmation_title),
            style = MaterialTheme.typography.headlineSmall,
            color = Theme.colors.onSurface,
        )
        Spacer(Modifier.height(12.dp))
        PhoneConfirmationText(
            onMoreInfoClick = { showBottomSheet = true }
        )
        Spacer(Modifier.height(32.dp))

        when (state) {
            is PhoneVerificationState.PhoneEnter -> {
                AnimatedVisibility(state.isCheckingPhone) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = Theme.colors.brand,
                        strokeWidth = 2.dp
                    )
                }
                PhoneTextFieldWithCountry(
                    phoneNumber = state.phoneNumber,
                    phoneCode = state.phoneCode,
                    defaultLang = state.defaultLang,
                    isValidPhone = state.isValidPhone,
                    onPickedCountry = { code, lang ->
                        onAction(
                            PhoneVerificationAction.OnCountryPicked(code, lang)
                        )
                    },
                    onPhoneValueChange = {
                        onAction(PhoneVerificationAction.OnPhoneNumberChanged(it))
                    }
                )
                Spacer(Modifier.height(12.dp))
                AnimatedVisibility(visible = state.isValidPhone) {
                    OpenTelegramButton(
                        onClick = { onAction(PhoneVerificationAction.OnOpenTelegramClick) },
                    )
                }
            }

            is PhoneVerificationState.CodeVerify -> {
                BackHandler(
                    onBack = {
                        onAction(PhoneVerificationAction.OnMoveToPhoneNumber)
                    }
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                }

                LaunchedEffect(state.isOtpFilled) {
                    if (state.isOtpFilled) {
                        keyboardController?.hide()
                    }
                }

                Text(
                    text = stringResource(R.string.enter_code_from_telegram),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Theme.colors.onSurface,
                )
                Spacer(Modifier.height(24.dp))
                OtpInputField(
                    modifier = Modifier.focusRequester(focusRequester),
                    otpText = state.otpValue,
                    onOtpModified = { value, isFilled ->
                        onAction(PhoneVerificationAction.OnOtpChange(
                            value, isFilled
                        ))
                    },
                    shouldShowCursor = true,
                    shouldCursorBlink = true
                )
                Spacer(Modifier.height(24.dp))
                AnimatedVisibility(state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = Theme.colors.brand,
                        strokeWidth = 2.dp
                    )
                }
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
            state = PhoneVerificationState.PhoneEnter(),
            onAction = {}
        )
    }
}