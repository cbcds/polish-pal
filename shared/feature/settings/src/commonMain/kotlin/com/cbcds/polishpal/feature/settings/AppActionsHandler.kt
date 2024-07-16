package com.cbcds.polishpal.feature.settings

import androidx.compose.runtime.Composable

internal interface AppActionsHandler {

    fun sendFeedback()

    fun shareApp()

    fun rateApp()
}

@Composable
internal expect fun rememberAppActionsHandler(): AppActionsHandler
