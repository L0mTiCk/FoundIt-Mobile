package com.l0mtick.founditmobile.start.presentation.phoneverify

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarManager
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarType
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.common.presentation.util.UiText
import com.l0mtick.founditmobile.start.domain.repository.AuthRepository
import com.simon.xmaterialccp.data.utils.checkPhoneNumber
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhoneVerificationViewModel(
    private val authRepository: AuthRepository,
    private val snackbarManager: SnackbarManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs: NavigationRoute.Start.PhoneVerification = savedStateHandle.toRoute()

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<PhoneVerificationEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state =
        MutableStateFlow<PhoneVerificationState>(PhoneVerificationState.PhoneEnter())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PhoneVerificationState.PhoneEnter()
        )

    fun onAction(action: PhoneVerificationAction) {
        when (action) {
            is PhoneVerificationAction.OnCountryPicked -> countryPicked(
                action.phoneCode,
                action.countryLang
            )

            is PhoneVerificationAction.OnPhoneNumberChanged -> phoneNumberChanged(action.newPhone)
            PhoneVerificationAction.OnOpenTelegramClick -> openTelegramBot()
            is PhoneVerificationAction.OnMoveToCode -> _state.update {
                PhoneVerificationState.CodeVerify(
                    action.fullPhoneNumber
                )
            }

            PhoneVerificationAction.OnMoveToPhoneNumber -> _state.update { PhoneVerificationState.PhoneEnter() }
            is PhoneVerificationAction.OnOtpChange -> otpChange(action.value, action.isOtpFilled)
        }
    }

    private fun countryPicked(phoneCode: String, lang: String) {
        val current = _state.value
        if (current !is PhoneVerificationState.PhoneEnter) return

        _state.update {
            current.copy(
                phoneCode = phoneCode,
                defaultLang = lang,
                isValidPhone = false,
                phoneNumber = ""
            )
        }
    }

    private fun phoneNumberChanged(phoneNumber: String) {
        val current = _state.value
        if (current !is PhoneVerificationState.PhoneEnter) return

        if (phoneNumber < current.phoneNumber || !current.isValidPhone || !current.isCheckingPhone) {
            val isValud = checkPhoneNumber(
                phoneNumber,
                "${current.phoneCode}${phoneNumber}",
                current.defaultLang
            )
            if (isValud) {
                viewModelScope.launch {
                    _state.update {
                        current.copy(
                            isCheckingPhone = true,
                            phoneNumber = phoneNumber
                        )
                    }
                    delay(300)
                    val fullNumber = "${current.phoneCode}${phoneNumber}".replace("+", "")
                    val isAvailable = authRepository.checkPhoneAvailability(fullNumber)
                    Log.d("asasda", isAvailable.toString())
                    when (isAvailable) {
                        is Result.Success -> {
                            _state.update {
                                current.copy(
                                    isValidPhone = true,
                                    isCheckingPhone = false,
                                    phoneNumber = phoneNumber
                                )
                            }
                        }

                        is Result.Error -> {
                            _state.update {
                                current.copy(
                                    isCheckingPhone = false,
                                    isValidPhone = false
                                )
                            }
                            when(isAvailable.error) {
                                DataError.Network.BAD_REQUEST -> snackbarManager.showSnackbar(
                                    UiText.StringResource(R.string.signup_phone_bad),
                                    SnackbarType.ERROR
                                )
                                DataError.Network.CONFLICT -> snackbarManager.showSnackbar(
                                    UiText.StringResource(R.string.signup_phone_taken),
                                    SnackbarType.ERROR
                                )
                                else -> snackbarManager.showError(isAvailable.error)
                            }
                        }
                    }
                }
            } else {
                _state.update {
                    current.copy(
                        phoneNumber = phoneNumber,
                        isValidPhone = false
                    )
                }
            }
        }
    }

    private fun openTelegramBot() {
        val current = _state.value
        if (current !is PhoneVerificationState.PhoneEnter) return

        if (current.isValidPhone) {
            viewModelScope.launch {
                val fullPhoneNumber = "${current.phoneCode}${current.phoneNumber}".replace("+", "")
                Log.w("phone_number", "Full phone: $fullPhoneNumber")
                eventChannel.send(PhoneVerificationEvent.OpenTelegramBot(fullPhoneNumber))
            }
        }
    }

    private fun otpChange(value: String, isFilled: Boolean) {
        var current = _state.value
        if (current !is PhoneVerificationState.CodeVerify) return

        current = current.copy(
            otpValue = value,
            isOtpFilled = isFilled,
            isLoading = isFilled
        )
        _state.update {
            current
        }

        if (isFilled) {
            viewModelScope.launch {
                val verifyResult = authRepository.verifyPhone(
                    phone = current.fullPhoneNumber,
                    code = current.otpValue
                )
                when (verifyResult) {
                    is Result.Success -> {
                        val registerResult = authRepository.register(
                            username = navArgs.login,
                            email = navArgs.email,
                            password = navArgs.pass,
                            phoneNumber = current.fullPhoneNumber
                        )
                        when (registerResult) {
                            is Result.Success -> {
                                eventChannel.send(PhoneVerificationEvent.OnVerificationSuccess)
                            }

                            is Result.Error -> {
                                Log.e("REGISTRATION", "Registration error: ${registerResult.error}")
                                snackbarManager.showSnackbar(
                                    UiText.StringResource(R.string.signup_error_register),
                                    SnackbarType.ERROR
                                )
                                _state.update {
                                    current.copy(
                                        isLoading = false
                                        // Potentially add an error message state here
                                    )
                                }
                            }
                        }
                    }

                    is Result.Error<*, *> -> {
                        Log.e("OTP", "OTP confirm error: ${verifyResult.error}")
                        snackbarManager.showSnackbar(
                            UiText.StringResource(R.string.signup_error_otp),
                            SnackbarType.ERROR
                        )
                        _state.update {
                            current.copy(
                                isLoading = false,
                                otpValue = ""
                            )
                        }
                    }
                }
            }
        }
    }
}