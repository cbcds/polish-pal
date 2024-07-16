package com.cbcds.polishpal.feature.settings.di

import com.cbcds.polishpal.feature.settings.AppLifecycleObserver
import com.cbcds.polishpal.feature.settings.notifications.AlarmsEnabledChecker
import com.cbcds.polishpal.feature.settings.notifications.AndroidNotificationsScheduler
import com.cbcds.polishpal.feature.settings.notifications.CancelScheduledNotificationsIfDisabledUseCase
import com.cbcds.polishpal.feature.settings.notifications.NotificationChannelCreator
import com.cbcds.polishpal.feature.settings.notifications.NotificationsEnabledChecker
import com.cbcds.polishpal.feature.settings.notifications.NotificationsScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal actual val nativeSettingsFeatureModule = module {
    factory { NotificationChannelCreator(androidContext()) }
    factory<NotificationsEnabledChecker> { NotificationsEnabledChecker(androidContext()) }
    factory<AlarmsEnabledChecker> { AlarmsEnabledChecker(androidContext()) }
    factory<NotificationsScheduler> {
        AndroidNotificationsScheduler(
            context = androidContext(),
            notificationsEnabledChecker = get(),
            alarmsEnabledChecker = get(),
        )
    }
    factoryOf(::CancelScheduledNotificationsIfDisabledUseCase)

    single(createdAtStart = true) {
        AppLifecycleObserver(
            lifecycleOwner = get(),
            cancelNotificationsIfPermissionRevokedUseCase = get(),
        )
    }
}
