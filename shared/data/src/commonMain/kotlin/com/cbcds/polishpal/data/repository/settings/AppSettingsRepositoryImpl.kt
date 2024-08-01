package com.cbcds.polishpal.data.repository.settings

import com.cbcds.polishpal.data.datasource.LocaleProvider
import com.cbcds.polishpal.data.datasource.prefs.Preferences
import com.cbcds.polishpal.data.model.settings.AppSettings
import com.cbcds.polishpal.data.model.settings.Locale
import com.cbcds.polishpal.data.model.settings.Theme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalTime

internal class AppSettingsRepositoryImpl(
    private val preferences: Preferences,
    private val localeProvider: LocaleProvider,
    externalScope: CoroutineScope,
    ioDispatcher: CoroutineDispatcher,
) : AppSettingsRepository {

    private val coroutineScope = CoroutineScope(externalScope.coroutineContext + ioDispatcher)

    override val settings: StateFlow<AppSettings?> = readSettings()
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override fun setTheme(theme: Theme) {
        preferences.writeString(KEY_THEME, theme.name)
    }

    override fun setLocale(locale: Locale) {
        localeProvider.setLocale(locale)
    }

    override fun setNotificationsEnabled(enabled: Boolean) {
        preferences.writeBoolean(KEY_NOTIFICATIONS_ENABLED, enabled)
    }

    override fun setNotificationsTime(time: LocalTime) {
        preferences.writeInt(KEY_NOTIFICATIONS_TIME_SECONDS, time.toSecondOfDay())
    }

    private fun readSettings(): Flow<AppSettings?> {
        return combine(
            localeProvider.locale,
            preferences.readString(KEY_THEME),
            preferences.readBoolean(KEY_NOTIFICATIONS_ENABLED),
            preferences.readInt(KEY_NOTIFICATIONS_TIME_SECONDS),
        ) { locale, theme, notificationsEnabled, notificationsTimeSeconds ->
            AppSettings(
                locale = locale,
                theme = theme
                    ?.let { runCatching { Theme.valueOf(it) }.getOrNull() }
                    ?: Theme.SYSTEM,
                notificationsEnabled = notificationsEnabled ?: false,
                notificationsTime = notificationsTimeSeconds
                    ?.let { runCatching { LocalTime.fromSecondOfDay(it) }.getOrNull() }
                    ?: LocalTime.fromSecondOfDay(DEFAULT_NOTIFICATION_TIME_SECONDS),
            )
        }
    }

    private companion object {

        const val KEY_THEME = "app_theme"
        const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        const val KEY_NOTIFICATIONS_TIME_SECONDS = "notifications_time_seconds"

        const val DEFAULT_NOTIFICATION_TIME_SECONDS = 18 * 60 * 60
    }
}
