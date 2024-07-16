package com.cbcds.polishpal.feature.settings.notifications

import com.cbcds.polishpal.data.repository.AppSettingsRepository

internal class CancelScheduledNotificationsIfDisabledUseCase(
    private val notificationScheduler: NotificationsScheduler,
    private val settingsRepository: AppSettingsRepository,
) {

    operator fun invoke() {
        if (!notificationScheduler.canScheduleNotifications()) {
            notificationScheduler.cancelNotifications()
            settingsRepository.setNotificationsEnabled(false)
        }
    }
}
