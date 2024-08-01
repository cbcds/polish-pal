package com.cbcds.polishpal.data.repository.settings

import com.cbcds.polishpal.data.datasource.prefs.Preferences
import kotlinx.coroutines.flow.firstOrNull

internal class PermissionsRepositoryImpl(
    private val preferences: Preferences,
) : PermissionsRepository {

    override suspend fun isNotificationsPermissionRequested(): Boolean {
        return preferences.readBoolean(KEY_NOTIFICATIONS).firstOrNull() ?: false
    }

    override fun setNotificationsPermissionRequested() {
        preferences.writeBoolean(KEY_NOTIFICATIONS, true)
    }

    private companion object {

        const val KEY_NOTIFICATIONS = "permission_notifications"
    }
}
