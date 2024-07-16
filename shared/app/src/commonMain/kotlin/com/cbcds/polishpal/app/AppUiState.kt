package com.cbcds.polishpal.app

internal sealed interface AppUiState {

    data object Idle : AppUiState

    data class Loaded(
        val darkThemeEnabled: Boolean?,
    ) : AppUiState
}
