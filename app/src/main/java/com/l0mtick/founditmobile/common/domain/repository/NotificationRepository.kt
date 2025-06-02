package com.l0mtick.founditmobile.common.domain.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result

interface NotificationRepository {
    /**
     * Отправляет токен Firebase на сервер
     * 
     * @param token Токен Firebase для push-уведомлений
     * @return Результат операции
     */
    suspend fun sendPushToken(token: String): Result<Unit, DataError.Network>
    suspend fun saveLocalToken(token: String)
    suspend fun areNotificationsEnabled(): Boolean
}