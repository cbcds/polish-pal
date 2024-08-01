package com.cbcds.polishpal.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbcds.polishpal.data.model.settings.AppSettings
import com.cbcds.polishpal.data.model.settings.Theme
import com.cbcds.polishpal.data.repository.settings.AppSettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class AppViewModel(
    settingsRepository: AppSettingsRepository,
) : ViewModel() {

    val uiState: StateFlow<AppUiState> = settingsRepository.settings
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppUiState.Idle)

    private fun AppSettings?.toUiState(): AppUiState {
        return this
            ?.let { settings ->
                AppUiState.Loaded(
                    darkThemeEnabled = settings.theme
                        .takeUnless { it == Theme.SYSTEM }
                        ?.let { it == Theme.DARK }
                )
            }
            ?: AppUiState.Idle
    }
}
