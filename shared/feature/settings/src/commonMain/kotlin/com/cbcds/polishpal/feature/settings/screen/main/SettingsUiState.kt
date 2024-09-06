package com.cbcds.polishpal.feature.settings.screen.main

import com.cbcds.polishpal.data.model.settings.AppSettings
import com.cbcds.polishpal.feature.settings.model.AppInfo

internal sealed interface SettingsUiState {

    data object Idle : SettingsUiState

    data class Loaded(
        val appInfo: AppInfo,
        val settings: AppSettings,
    ) : SettingsUiState
}
