package com.cbcds.polishpal.data.repository.settings

interface PermissionsRepository {

    suspend fun isNotificationsPermissionRequested(): Boolean

    fun setNotificationsPermissionRequested()
}
