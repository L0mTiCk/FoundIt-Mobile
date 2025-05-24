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
import com.l0mtick.founditmobile.common.domain.repository.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject

class FoundItFirebaseMessagingService : FirebaseMessagingService() {

    private val notificationRepository: NotificationRepository by inject()

    override fun onMessageReceived(message: RemoteMessage) {
        val areNotificationsEnabledGlobally = runBlocking {
            notificationRepository.areNotificationsEnabled()
        }
        if (!checkNotificationPermission() && !areNotificationsEnabledGlobally) {
            Log.d(TAG, "Notifications skipped due to permissions or app settings.")
            return
        }

        val data = message.data
        Log.d(TAG, "Received FCM data: $data")
        val notificationType = data["notification_type"]

        when (notificationType) {
            "NEW_CHAT_MESSAGE" -> handleNewChatMessage(data)
            "MODERATION_SUCCESS" -> handleModerationSuccess(data)
            else -> Log.w(TAG, "Received unknown notification type: $notificationType or type is missing.")
        }
    }

    private fun handleNewChatMessage(data: Map<String, String>) {
        val chatId = data["chat_id"]?.toIntOrNull()
        if (chatId == null) {
            Log.w(TAG, "Chat ID missing for NEW_CHAT_MESSAGE notification.")
            return
        }
        val title = getLocalizedTitle(data["title_loc_key"], data["title_loc_args"])
        val body = getLocalizedBody(null, null, data["body_text"])

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("chat_id", chatId)
            putExtra("notification_type", "NEW_CHAT_MESSAGE")
        }
        val pendingIntent = PendingIntent.getActivity(
            this, chatId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        showNotification(
            notificationId = chatId,
            title = title,
            body = body,
            pendingIntent = pendingIntent,
            channelId = NotificationHelper.CHANNEL_ID_CHAT
        )
    }

    private fun handleModerationSuccess(data: Map<String, String>) {
        val title = getLocalizedTitle(data["title_loc_key"], data["title_loc_args"])
        val body = getLocalizedBody(data["body_loc_key"], null, null)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("notification_type", "MODERATION_SUCCESS")
        }
        val notificationId = System.currentTimeMillis().toInt()
        val requestCode = notificationId
        val pendingIntent = PendingIntent.getActivity(
            this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        showNotification(
            notificationId = notificationId,
            title = title,
            body = body,
            pendingIntent = pendingIntent,
            channelId = NotificationHelper.CHANNEL_ID_MODERATION
        )
    }

    private fun showNotification(
        notificationId: Int,
        title: String,
        body: String,
        pendingIntent: PendingIntent,
        channelId: String
    ) {
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))

        val notificationManager = NotificationManagerCompat.from(this)
        if (checkNotificationPermission()) {
            try {
                notificationManager.notify(notificationId, notificationBuilder.build())
            } catch (e: SecurityException) {
                Log.e(TAG, "SecurityException on notify: ${e.message}", e)
            }
        } else {
            Log.w(TAG, "Notification permission denied. Notification not shown.")
        }
    }



    override fun onNewToken(token: String) {
        Log.d(TAG, "New FCM token: $token")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                notificationRepository.saveLocalToken(token)
                val result = notificationRepository.sendPushToken(token)
                Log.d(TAG, "Push token send attempt finished: $result")
            } catch (e: Exception) {
                Log.e(TAG, "Error handling new push token", e)
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

    private fun parseJsonArgs(argsJson: String?): List<String> {
        return try {
            if (argsJson.isNullOrBlank()) emptyList() else Json.decodeFromString<List<String>>(argsJson)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to parse localization args: '$argsJson'", e)
            emptyList()
        }
    }

    private fun getLocalizedTitle(key: String?, argsJson: String?): String {
        val args = parseJsonArgs(argsJson)

        val titleResId = when (key) {
            "new_message_notification_title" -> R.string.new_message_notification_title
            "moderation_success_notification_title" -> R.string.moderation_success_notification_title
            else -> {
                Log.w(TAG, "Unknown title_loc_key: '$key'")
                R.string.default_notification_title
            }
        }

        return try {
            if (args.isEmpty() && titleResId == R.string.default_notification_title) {
                getString(titleResId)
            } else if (args.isEmpty() && (titleResId == R.string.new_message_notification_title || titleResId == R.string.moderation_success_notification_title)) {
                Log.w(TAG, "Missing arguments for title key: $key (resId: $titleResId)")
                getString(R.string.default_notification_title)
            }
            else {
                getString(titleResId, *args.toTypedArray())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error formatting localized title for key '$key' with args '$args'", e)
            getString(R.string.default_notification_title)
        }
    }

    private fun getLocalizedBody(key: String?, argsJson: String?, directBody: String?): String {
        if (!directBody.isNullOrBlank()) {
            return directBody
        }

        val args = parseJsonArgs(argsJson)

        val bodyResId = when (key) {
            "moderation_success_notification_body" -> R.string.moderation_success_notification_body
            else -> {
                Log.w(TAG, "Unknown body_loc_key: '$key', and no direct_body provided.")
                R.string.default_notification_body
            }
        }

        return try {
            if (args.isEmpty() && bodyResId == R.string.default_notification_body) {
                getString(bodyResId)
            } else if (args.isEmpty() && bodyResId == R.string.moderation_success_notification_body) {
                getString(bodyResId)
            }
            else {
                getString(bodyResId, *args.toTypedArray())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error formatting localized body for key '$key' with args '$args'", e)
            getString(R.string.default_notification_body)
        }
    }

    companion object {
        private const val TAG = "FoundItFirebaseMsgSvc"
    }
}
