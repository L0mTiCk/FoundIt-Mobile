package com.l0mtick.founditmobile.common.data.snackbar

import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.presentation.util.UiText

/**
 * Примеры использования SnackbarManager в приложении
 */
object SnackbarExamples {
    
    /**
     * Показать успешное сообщение с обычной строкой
     */
    fun showSuccessMessage(snackbarManager: SnackbarManager) {
        snackbarManager.showSuccess("Операция выполнена успешно!")
    }
    
    /**
     * Показать успешное сообщение со строковым ресурсом
     */
    fun showSuccessWithResource(snackbarManager: SnackbarManager) {
        snackbarManager.showSuccess(
            UiText.StringResource(R.string.app_description)
        )
    }
    
    /**
     * Показать ошибку из Error объекта
     */
    fun showNetworkError(snackbarManager: SnackbarManager) {
        snackbarManager.showError(DataError.Network.NO_INTERNET)
    }
    
    /**
     * Показать предупреждение
     */
    fun showWarning(snackbarManager: SnackbarManager) {
        snackbarManager.showSnackbar(
            message = "Внимание! Проверьте введенные данные",
            type = SnackbarType.WARNING
        )
    }
    
    /**
     * Показать информационное сообщение
     */
    fun showInfo(snackbarManager: SnackbarManager) {
        snackbarManager.showSnackbar(
            message = UiText.DynamicString("Информация обновлена"),
            type = SnackbarType.INFO,
            duration = 5000L // 5 секунд
        )
    }
    
    /**
     * Показать ошибку с кастомным временем показа
     */
    fun showCustomError(snackbarManager: SnackbarManager) {
        snackbarManager.showSnackbar(
            message = "Произошла критическая ошибка",
            type = SnackbarType.ERROR,
            duration = 6000L // 6 секунд
        )
    }
}