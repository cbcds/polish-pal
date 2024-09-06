package com.cbcds.polishpal.feature.settings.notifications

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationChannel as AndroidNotificationChannel

class NotificationChannelCreator(private val context: Context) {

    fun createNotificationChannels() {
        val notificationManager = NotificationManagerCompat.from(context)

        for (channel in NotificationChannel.entries) {
            notificationManager.createNotificationChannel(
                AndroidNotificationChannel(
                    channel.id,
                    context.getString(channel.nameResId),
                    channel.importance,
                )
            )
        }
    }
}
