package com.cbcds.polishpal.feature.settings.notifications

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat

internal class NotificationsEnabledChecker(context: Context) {

    private val notificationManager = NotificationManagerCompat.from(context)

    fun areNotificationsEnabledForApp(): Boolean {
        return notificationManager.areNotificationsEnabled()
    }

    fun areNotificationsEnabledForChannel(channel: NotificationChannel): Boolean {
        var notificationsEnabled = areNotificationsEnabledForApp()

        if (notificationsEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelImportance = notificationManager
                .getNotificationChannel(channel.id)
                ?.importance
                ?: NotificationManager.IMPORTANCE_NONE

            notificationsEnabled = channelImportance != NotificationManager.IMPORTANCE_NONE
        }

        return notificationsEnabled
    }
}
