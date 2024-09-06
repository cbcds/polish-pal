package com.cbcds.polishpal.feature.settings.screen.main

import androidx.compose.runtime.Composable
import com.cbcds.polishpal.feature.settings.model.AppInfo

internal interface AppActionsHandler {

    fun sendFeedback()

    fun shareApp()

    fun rateApp()
}

@Composable
internal expect fun rememberAppActionsHandler(appInfo: AppInfo): AppActionsHandler
