package com.l0mtick.founditmobile.common.data.notification

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.common.domain.repository.NotificationRepository
import com.l0mtick.founditmobile.main.domain.repository.MainApi

class NotificationRepositoryImpl(
    private val mainApi: MainApi,
    private val localStorage: LocalStorage
) : NotificationRepository {

    override suspend fun sendPushToken(token: String): Result<Unit, DataError.Network> {
        if (!localStorage.isLoggedIn()) {
            return Result.Error(DataError.Network.UNAUTHORIZED)
        }

        return mainApi.sendUserPushToken(
            token = token
        )
    }

    override suspend fun saveLocalToken(token: String) {
        localStorage.setPushToken(token)
    }
}