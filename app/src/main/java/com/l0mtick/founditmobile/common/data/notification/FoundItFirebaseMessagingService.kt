package com.l0mtick.founditmobile.common.data.notification

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.l0mtick.founditmobile.MainActivity
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject

class FoundItFirebaseMessagingService: FirebaseMessagingService()  {

    private val notificationRepository: NotificationRepository by inject()

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data

        val chatId = data["chat_id"]?.toIntOrNull() ?: return
        val content = data["body_text"] ?: return

        val titleKey = data["title_loc_key"]
        val titleArgsKey = data["title_loc_args"]

        val title = getLocalizedTitle(
            key = titleKey,
            argsJson = titleArgsKey
        )
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("chat_id", chatId)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, chatId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
            
        val notificationManager = NotificationManagerCompat.from(this)
        if (checkNotificationPermission()) {
            try {
                notificationManager.notify(chatId, notification)
            } catch (e: SecurityException) {
                // Обработка исключения, если разрешения были отозваны
            }
        }
    }

    override fun onNewToken(token: String) {
        // Отправляем токен на сервер, но только если пользователь авторизован
        CoroutineScope(Dispatchers.IO).launch {
            try {
                notificationRepository.saveLocalToken(token)
                val result = notificationRepository.sendPushToken(token)
                when (result) {
                    is Result.Success -> {
                        Log.d(TAG, "Push token успешно отправлен на сервер")
                    }
                    is Result.Error -> {
                        Log.w(TAG, "Не удалось отправить push token: ${result.error}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при отправке push token", e)
            }
        }
    }
    
    private fun checkNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    companion object {
        private const val TAG = "FirebaseMessaging"
    }
    
    private fun getLocalizedTitle(key: String?, argsJson: String?): String {
        return when (key) {
            "new_message_notification_title" -> {
                val args = try {
                    Json.decodeFromString<List<String>>(argsJson ?: "")
                } catch (_: Exception) {
                    null
                }
                if (args != null && args.size >= 2) {
                    getString(R.string.new_message_notification_title, args[0], args[1])
                } else {
                    getString(R.string.new_message_fallback_title)
                }
            }
            else -> getString(R.string.new_message_fallback_title)
        }
    }

}