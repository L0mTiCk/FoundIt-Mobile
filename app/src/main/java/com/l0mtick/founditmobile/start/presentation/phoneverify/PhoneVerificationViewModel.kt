package com.l0mtick.founditmobile.start.presentation.phoneverify

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.start.domain.repository.AuthRepository
import com.simon.xmaterialccp.data.utils.checkPhoneNumber
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhoneVerificationViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<PhoneVerificationEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow<PhoneVerificationState>(PhoneVerificationState.PhoneEnter())
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
            is PhoneVerificationAction.OnCountryPicked -> countryPicked(action.phoneCode, action.countryLang)
            is PhoneVerificationAction.OnPhoneNumberChanged -> phoneNumberChanged(action.newPhone)
            PhoneVerificationAction.OnOpenTelegramClick -> openTelegramBot()
            is PhoneVerificationAction.OnMoveToCode -> _state.update { PhoneVerificationState.CodeVerify(action.fullPhoneNumber) }
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

        if (phoneNumber < current.phoneNumber || !current.isValidPhone) {
            val isValud = checkPhoneNumber(
                phoneNumber,
                "${current.phoneCode}${phoneNumber}",
                current.defaultLang
            )
            _state.update {
                current.copy(
                    phoneNumber = phoneNumber,
                    isValidPhone = isValud
                )
            }
        }
    }

    private fun openTelegramBot() {
        val current = _state.value
        if (current !is PhoneVerificationState.PhoneEnter) return

        if (current.isValidPhone) {
            viewModelScope.launch {
                val fullPhoneNumber = "${current.phoneCode}${current.phoneNumber}".replace("+", "")
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
                val result = authRepository.verifyPhone(
                    phone = current.fullPhoneNumber,
                    code = current.otpValue
                )
                when(result) {
                    is Result.Success<*, *> -> {
                        Log.d("OTP", "OTP Confirmed")
                    }
                    is Result.Error<*, *> -> {
                        Log.e("OTP", "OTP confirm error: ${result.error}")
                    }
                }
                _state.update {
                    current.copy(
                        isLoading = false
                    )
                }
            }
        }
    }


}