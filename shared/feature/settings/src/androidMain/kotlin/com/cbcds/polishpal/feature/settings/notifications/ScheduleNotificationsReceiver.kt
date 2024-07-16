package com.cbcds.polishpal.feature.settings.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cbcds.polishpal.core.kotlin.di.IO_DISPATCHER
import com.cbcds.polishpal.data.repository.AppSettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class ScheduleNotificationsReceiver : BroadcastReceiver(), KoinComponent {

    private val settingsRepository: AppSettingsRepository by inject()
    private val notificationsScheduler: NotificationsScheduler by inject()
    private val ioDispatcher: CoroutineDispatcher by inject(named(IO_DISPATCHER))

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED ||
            !notificationsScheduler.canScheduleNotifications()
        ) {
            return
        }

        val pendingResult = goAsync()
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(ioDispatcher) {
            try {
                settingsRepository.settings.first { it != null }?.let { settings ->
                    if (settings.notificationsEnabled) {
                        notificationsScheduler.scheduleNotifications(settings.notificationsTime)
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
