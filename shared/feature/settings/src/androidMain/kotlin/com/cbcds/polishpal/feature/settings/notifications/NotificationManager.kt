package com.cbcds.polishpal.feature.settings.notifications

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.cbcds.polishpal.feature.settings.R
import android.app.NotificationManager as AndroidNotificationManager

internal class NotificationManager(private val context: Context) {

    private val androidNotificationManager = context.getSystemService<AndroidNotificationManager>()

    fun notify(
        id: Int,
        channel: NotificationChannel,
        text: String,
        pendingIntent: PendingIntent? = null,
    ) {
        val notification = NotificationCompat.Builder(context, channel.id)
            .setContentTitle(text)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(channel.importance)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        androidNotificationManager?.notify(id, notification)
    }
}
