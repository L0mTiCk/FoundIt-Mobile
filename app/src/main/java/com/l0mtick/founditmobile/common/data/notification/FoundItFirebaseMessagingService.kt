package com.l0mtick.founditmobile.common.data.notification

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.l0mtick.founditmobile.MainActivity
import com.l0mtick.founditmobile.R
import kotlinx.serialization.json.Json

class FoundItFirebaseMessagingService: FirebaseMessagingService()  {

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

        val notification = NotificationCompat.Builder(this, "chat_messages")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        //TODO: handle notification check
        NotificationManagerCompat.from(this).notify(chatId, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
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