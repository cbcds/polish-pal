package com.cbcds.polishpal.feature.settings.navigation

import androidx.compose.runtime.Composable
import com.cbcds.polishpal.core.navigation.AppScreen
import com.cbcds.polishpal.feature.settings.SettingsScreen

object SettingsScreen : AppScreen {

    override val fullscreen = false

    @Composable
    override fun Content() {
        SettingsScreen()
    }
}
