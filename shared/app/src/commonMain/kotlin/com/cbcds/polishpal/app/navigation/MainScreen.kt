package com.cbcds.polishpal.app.navigation

import androidx.compose.runtime.Composable
import com.cbcds.polishpal.app.MainScreen
import com.cbcds.polishpal.core.navigation.AppScreen

object MainScreen : AppScreen {

    override val fullscreen = false

    @Composable
    override fun Content() {
        MainScreen()
    }
}
