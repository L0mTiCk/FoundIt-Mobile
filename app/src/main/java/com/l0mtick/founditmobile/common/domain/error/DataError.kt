package com.l0mtick.founditmobile.common.domain.error

sealed interface DataError : Error {
    enum class Network : DataError {
        BAD_REQUEST,
        UNAUTHORIZED,
        CONFLICT,
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL
    }
}