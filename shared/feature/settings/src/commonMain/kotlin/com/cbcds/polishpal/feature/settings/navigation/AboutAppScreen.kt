package com.cbcds.polishpal.feature.settings.navigation

import androidx.compose.runtime.Composable
import com.cbcds.polishpal.core.navigation.AppScreen
import com.cbcds.polishpal.feature.settings.screen.about.AboutAppScreen

internal object AboutAppScreen : AppScreen {

    override val fullscreen = true

    @Composable
    override fun Content() {
        AboutAppScreen()
    }
}
