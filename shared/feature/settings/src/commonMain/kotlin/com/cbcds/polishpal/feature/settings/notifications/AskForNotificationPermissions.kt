package com.cbcds.polishpal.feature.settings.notifications

import androidx.compose.runtime.Composable

@Composable
internal expect fun AskForNotificationPermissions(
    onPermissionsGrantedChange: (Boolean) -> Unit,
)
