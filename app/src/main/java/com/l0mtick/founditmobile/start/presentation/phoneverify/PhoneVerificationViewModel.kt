package com.l0mtick.founditmobile.start.presentation.phoneverify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simon.xmaterialccp.data.utils.checkPhoneNumber
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhoneVerificationViewModel : ViewModel() {

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
                eventChannel.send(PhoneVerificationEvent.OpenTelegramBot)
            }
        }
    }


}