package com.cbcds.polishpal.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbcds.polishpal.data.model.settings.AppSettings
import com.cbcds.polishpal.data.model.settings.Locale
import com.cbcds.polishpal.data.model.settings.Theme
import com.cbcds.polishpal.data.repository.settings.AppSettingsRepository
import com.cbcds.polishpal.feature.settings.notifications.NotificationsScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalTime

internal class SettingsViewModel(
    private val settingsRepository: AppSettingsRepository,
    private val notificationsScheduler: NotificationsScheduler,
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = settingsRepository.settings
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, SettingsUiState.Idle)

    private val _uiEffect = MutableStateFlow<SettingsUiEffect?>(null)
    val uiEffect: StateFlow<SettingsUiEffect?> = _uiEffect.asStateFlow()

    fun setTheme(theme: Theme) {
        settingsRepository.setTheme(theme)
    }

    fun setLocale(locale: Locale) {
        settingsRepository.setLocale(locale)
    }

    fun onNotificationsEnabledChange(enabled: Boolean) {
        if (enabled) {
            _uiEffect.value = SettingsUiEffect.AskForNotificationsPermission
        } else {
            disableNotifications()
        }
    }

    fun onNotificationPermissionsGrantedChange(granted: Boolean) {
        _uiEffect.value = null
        getNotificationsTime()?.takeIf { granted }
            ?.let { notificationsTime -> enableNotifications(notificationsTime) }
            ?: disableNotifications()
    }

    fun onNotificationsTimeChange(time: LocalTime) {
        settingsRepository.setNotificationsTime(time)
        enableNotifications(time)
    }

    private fun enableNotifications(time: LocalTime) {
        settingsRepository.setNotificationsEnabled(true)
        notificationsScheduler.cancelNotifications()
        notificationsScheduler.scheduleNotifications(time)
    }

    private fun disableNotifications() {
        settingsRepository.setNotificationsEnabled(false)
        notificationsScheduler.cancelNotifications()
    }

    private fun getNotificationsTime(): LocalTime? {
        return (uiState.value as? SettingsUiState.Loaded)?.settings?.notificationsTime
    }

    private fun AppSettings?.toUiState(): SettingsUiState {
        return this?.let(SettingsUiState::Loaded) ?: SettingsUiState.Idle
    }
}
