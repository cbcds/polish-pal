package com.cbcds.polishpal.feature.settings

import com.cbcds.polishpal.data.model.AppSettings

internal sealed interface SettingsUiState {

    data object Idle : SettingsUiState

    data class Loaded(
        val settings: AppSettings,
    ) : SettingsUiState
}
