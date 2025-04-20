package com.l0mtick.founditmobile.common.presentation.util

import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.error.ValidationError
import com.l0mtick.founditmobile.common.domain.error.Error

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Network.BAD_REQUEST -> UiText.StringResource(
            R.string.bad_request
        )

        DataError.Network.CONFLICT -> UiText.StringResource(
            R.string.request_conflict
        )

        DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(
            R.string.the_request_timed_out
        )

        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(
            R.string.youve_hit_your_rate_limit
        )

        DataError.Network.NO_INTERNET -> UiText.StringResource(
            R.string.no_internet
        )

        DataError.Network.PAYLOAD_TOO_LARGE -> UiText.StringResource(
            R.string.file_too_large
        )

        DataError.Network.SERVER_ERROR -> UiText.StringResource(
            R.string.server_error
        )

        DataError.Network.SERIALIZATION -> UiText.StringResource(
            R.string.error_serialization
        )

        DataError.Network.UNKNOWN -> UiText.StringResource(
            R.string.unknown_error
        )

        DataError.Local.DISK_FULL -> UiText.StringResource(
            R.string.error_disk_full
        )

    }
}

fun ValidationError.asUiText(): UiText {
    return when (this) {
        ValidationError.EMAIL_EMPTY -> UiText.StringResource(R.string.email_empty)
        ValidationError.EMAIL_INVALID -> UiText.StringResource(R.string.invalid_email)
        ValidationError.PASSWORD_EMPTY -> UiText.StringResource(R.string.password_empty)
        ValidationError.PASSWORD_TOO_SHORT -> UiText.StringResource(R.string.password_too_short)
        ValidationError.PASSWORD_NO_UPPERCASE -> UiText.StringResource(R.string.password_no_uppercase)
        ValidationError.PASSWORD_NO_SPECIAL_CHAR -> UiText.StringResource(R.string.password_no_special)
        ValidationError.PASSWORDS_DO_NOT_MATCH -> UiText.StringResource(R.string.passwords_do_not_match)
        ValidationError.USERNAME_EMPTY -> UiText.StringResource(R.string.username_empty)
        ValidationError.USERNAME_INVALID_CHARACTERS -> UiText.StringResource(R.string.username_wrong_symbols)
        ValidationError.USERNAME_TOO_SHORT -> UiText.StringResource(R.string.username_too_short)
        ValidationError.EMPTY -> UiText.StringResource(R.string.empty_field)
    }
}

fun Error.asUiText(): UiText = when (this) {
    is DataError -> this.asUiText()
    is ValidationError -> this.asUiText()
    else -> UiText.StringResource(R.string.unknown_error)
}

fun <E> Result.Error<*, E>.asErrorUiText(): UiText where E : Error {
    return (error as Error).asUiText()
}