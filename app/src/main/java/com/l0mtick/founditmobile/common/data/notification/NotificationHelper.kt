package com.l0mtick.founditmobile.common.data.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.l0mtick.founditmobile.R

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID_CHAT = "foundit_chat_messages"

        const val CHANNEL_ID_MODERATION = "foundit_moderation_updates"

        const val CHANNEL_ID_ITEM_UPDATES = "foundit_item_updates_channel" // Новый канал
    }

    fun createNotificationChannels() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val chatChannelName = context.getString(R.string.notification_channel_chat_name)
        val chatChannelDescription = context.getString(R.string.notification_channel_chat_description)
        val chatChannelImportance = NotificationManager.IMPORTANCE_HIGH
        val chatChannel = NotificationChannel(CHANNEL_ID_CHAT, chatChannelName, chatChannelImportance).apply {
            description = chatChannelDescription
        }
        notificationManager.createNotificationChannel(chatChannel)

        val moderationChannelName = context.getString(R.string.notification_channel_moderation_name)
        val moderationChannelDescription = context.getString(R.string.notification_channel_moderation_description)
        val moderationChannelImportance = NotificationManager.IMPORTANCE_HIGH
        val moderationChannel = NotificationChannel(CHANNEL_ID_MODERATION, moderationChannelName, moderationChannelImportance).apply {
            description = moderationChannelDescription
        }
        notificationManager.createNotificationChannel(moderationChannel)

        val itemUpdatesChannelName = context.getString(R.string.item_updates_channel_name)
        val itemUpdatesChannelDescription = context.getString(R.string.item_updates_channel_description)
        val itemUpdatesChannelImportance = NotificationManager.IMPORTANCE_HIGH
        val itemUpdatesChannel = NotificationChannel(CHANNEL_ID_ITEM_UPDATES,
            itemUpdatesChannelName, itemUpdatesChannelImportance).apply {
            description = itemUpdatesChannelDescription
        }
        notificationManager.createNotificationChannel(itemUpdatesChannel)

    }

    fun areNotificationsEnabled(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun requestNotificationPermission(permissionLauncher: ActivityResultLauncher<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}