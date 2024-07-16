package com.cbcds.polishpal.feature.settings.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService
import com.cbcds.polishpal.feature.settings.R
import com.cbcds.polishpal.feature.settings.notifications.ShowNotificationReceiver.Companion.EXTRA_NEXT_NOTIFICATION_TIME_SECONDS
import com.cbcds.polishpal.feature.settings.notifications.ShowNotificationReceiver.Companion.EXTRA_NOTIFICATION_CHANNEL
import com.cbcds.polishpal.feature.settings.notifications.ShowNotificationReceiver.Companion.EXTRA_NOTIFICATION_ID
import com.cbcds.polishpal.feature.settings.notifications.ShowNotificationReceiver.Companion.EXTRA_NOTIFICATION_PENDING_INTENT
import com.cbcds.polishpal.feature.settings.notifications.ShowNotificationReceiver.Companion.EXTRA_NOTIFICATION_TEXT_RES_ID
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

internal class AndroidNotificationsScheduler(
    private val notificationsEnabledChecker: NotificationsEnabledChecker,
    private val alarmsEnabledChecker: AlarmsEnabledChecker,
    private val context: Context,
) : NotificationsScheduler {

    private val alarmManager by lazy { context.getSystemService<AlarmManager>() }

    override fun canScheduleNotifications(): Boolean {
        return notificationsEnabledChecker
            .areNotificationsEnabledForChannel(NotificationChannel.LEARNING) &&
            alarmsEnabledChecker.areAlarmsEnabled()
    }

    override fun scheduleNotifications(time: LocalTime) {
        if (canScheduleNotifications()) {
            alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                getNotificationTimeInMillis(time),
                createShowLearningReminderIntent(time).toBroadcastReceiverPendingIntent(),
            )
        }
    }

    override fun cancelNotifications() {
        alarmManager?.cancel(createBaseShowNotificationIntent().toBroadcastReceiverPendingIntent())
    }

    private fun createShowLearningReminderIntent(time: LocalTime): Intent {
        val intent = createBaseShowNotificationIntent()
        intent.apply {
            putExtra(EXTRA_NOTIFICATION_ID, NotificationChannel.LEARNING.id.hashCode())
            putExtra(EXTRA_NOTIFICATION_CHANNEL, NotificationChannel.LEARNING.name)
            putExtra(EXTRA_NOTIFICATION_TEXT_RES_ID, R.string.notification_learning_reminder)
            putExtra(EXTRA_NOTIFICATION_PENDING_INTENT, createOpenAppPendingIntent())
            putExtra(EXTRA_NEXT_NOTIFICATION_TIME_SECONDS, time.toSecondOfDay())
        }

        return intent
    }

    private fun createBaseShowNotificationIntent(): Intent {
        return Intent(context.applicationContext, ShowNotificationReceiver::class.java)
    }

    private fun Intent.toBroadcastReceiverPendingIntent(): PendingIntent {
        return PendingIntent.getBroadcast(
            context.applicationContext,
            ShowNotificationReceiver.REQUEST_CODE,
            this,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    private fun createOpenAppPendingIntent(): PendingIntent {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)

        return PendingIntent.getActivity(
            context.applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun getNotificationTimeInMillis(time: LocalTime): Long {
        val timeZone = TimeZone.currentSystemDefault()
        val currentDateTime = Clock.System.now().toLocalDateTime(timeZone)

        val notificationDateTime = if (currentDateTime.time < time) {
            currentDateTime.date
        } else {
            currentDateTime.date.plus(DatePeriod(days = 1))
        }.atTime(time)

        return notificationDateTime.toInstant(timeZone).toEpochMilliseconds()
    }
}
