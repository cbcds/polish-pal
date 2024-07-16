package com.cbcds.polishpal.feature.settings.notifications

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.IntentCompat
import kotlinx.datetime.LocalTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.random.Random

class ShowNotificationReceiver : BroadcastReceiver(), KoinComponent {

    private val notificationsScheduler: NotificationsScheduler by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val channel = intent.getStringExtra(EXTRA_NOTIFICATION_CHANNEL)
                ?.let { runCatching { NotificationChannel.valueOf(it) }.getOrNull() }
                ?: return

            val text = intent.getIntExtra(EXTRA_NOTIFICATION_TEXT_RES_ID, -1)
                .takeIf { it != -1 }
                ?.let(context::getString)
                ?: return

            val id = intent.getIntExtra(EXTRA_NOTIFICATION_ID, Random.nextInt())

            val pendingIntent = IntentCompat.getParcelableExtra(
                intent,
                EXTRA_NOTIFICATION_PENDING_INTENT,
                PendingIntent::class.java,
            )

            val nextNotificationTime = intent.getIntExtra(EXTRA_NEXT_NOTIFICATION_TIME_SECONDS, -1)
                .takeIf { it != -1 }
                ?.let { runCatching { LocalTime.fromSecondOfDay(it) }.getOrNull() }
            if (nextNotificationTime != null) {
                notificationsScheduler.scheduleNotifications(nextNotificationTime)
            }

            NotificationManager(context).notify(
                id = id,
                channel = channel,
                text = text,
                pendingIntent = pendingIntent,
            )
        }
    }

    companion object {

        const val REQUEST_CODE = 1

        const val EXTRA_NOTIFICATION_CHANNEL = "notification_channel"
        const val EXTRA_NOTIFICATION_ID = "notification_id"
        const val EXTRA_NOTIFICATION_TEXT_RES_ID = "notification_text"
        const val EXTRA_NOTIFICATION_PENDING_INTENT = "pending_intent"
        const val EXTRA_NEXT_NOTIFICATION_TIME_SECONDS = "next_notification_time"
    }
}
