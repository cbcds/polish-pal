package com.cbcds.polishpal.feature.settings.screen.main

internal sealed interface SettingsUiEffect {

    data object AskForNotificationsPermission : SettingsUiEffect
}
