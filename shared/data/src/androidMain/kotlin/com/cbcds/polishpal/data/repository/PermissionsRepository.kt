package com.cbcds.polishpal.data.repository

import com.cbcds.polishpal.data.datasource.prefs.Preferences
import kotlinx.coroutines.flow.firstOrNull

class PermissionsRepository(
    private val preferences: Preferences,
) {

    suspend fun isNotificationsPermissionRequested(): Boolean {
        return preferences.readBoolean(KEY_NOTIFICATIONS).firstOrNull() ?: false
    }

    fun setNotificationsPermissionRequested() {
        preferences.writeBoolean(KEY_NOTIFICATIONS, true)
    }

    private companion object {

        const val KEY_NOTIFICATIONS = "permission_notifications"
    }
}
