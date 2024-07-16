package com.cbcds.polishpal.feature.settings

internal sealed interface SettingsUiEffect {

    data object AskForNotificationsPermission : SettingsUiEffect
}
