package com.cbcds.polishpal.data.repository.settings

import com.cbcds.polishpal.data.model.settings.AppSettings
import com.cbcds.polishpal.data.model.settings.Locale
import com.cbcds.polishpal.data.model.settings.Theme
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalTime

interface AppSettingsRepository {

    val settings: StateFlow<AppSettings?>

    fun setTheme(theme: Theme)

    fun setLocale(locale: Locale)

    fun setNotificationsEnabled(enabled: Boolean)

    fun setNotificationsTime(time: LocalTime)
}
