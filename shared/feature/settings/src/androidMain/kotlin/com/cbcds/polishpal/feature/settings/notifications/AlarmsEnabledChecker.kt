package com.cbcds.polishpal.feature.settings.notifications

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import androidx.core.content.getSystemService

internal class AlarmsEnabledChecker(context: Context) {

    private val alarmManager by lazy { context.getSystemService<AlarmManager>() }

    fun areAlarmsEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager?.canScheduleExactAlarms() == true
        } else {
            true
        }
    }
}
