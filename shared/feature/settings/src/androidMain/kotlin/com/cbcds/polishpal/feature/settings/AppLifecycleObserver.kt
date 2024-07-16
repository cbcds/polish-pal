package com.cbcds.polishpal.feature.settings

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.cbcds.polishpal.feature.settings.notifications.CancelScheduledNotificationsIfDisabledUseCase

internal class AppLifecycleObserver(
    lifecycleOwner: LifecycleOwner,
    private val cancelNotificationsIfPermissionRevokedUseCase: CancelScheduledNotificationsIfDisabledUseCase,
) : DefaultLifecycleObserver {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        cancelNotificationsIfPermissionRevokedUseCase()
    }
}
