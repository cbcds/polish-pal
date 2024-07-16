package com.cbcds.polishpal.feature.settings.notifications

import kotlinx.datetime.LocalTime

interface NotificationsScheduler {

    fun canScheduleNotifications(): Boolean

    fun scheduleNotifications(time: LocalTime)

    fun cancelNotifications()
}
