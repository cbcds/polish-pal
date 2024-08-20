package com.cbcds.polishpal.feature.settings.notifications

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.cbcds.polishpal.core.ui.component.LocalSnackbarHostState
import com.cbcds.polishpal.data.repository.settings.PermissionsRepository
import com.cbcds.polishpal.feature.settings.R
import com.cbcds.polishpal.shared.core.ui.Res
import com.cbcds.polishpal.shared.core.ui.ok
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.get

@Composable
internal actual fun AskForNotificationPermissions(
    onPermissionsGrantedChange: (Boolean) -> Unit,
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        AskForPermissionsPreTiramisu(onPermissionsGrantedChange)
    } else {
        AskForPermissionsPostTiramisu(onPermissionsGrantedChange)
    }
}

@Composable
private fun AskForPermissionsPreTiramisu(
    onPermissionsGrantedChange: (Boolean) -> Unit,
    notificationsEnabledChecker: NotificationsEnabledChecker = get(),
) {
    if (!notificationsEnabledChecker.areLearningNotificationsEnabled()) {
        val context = LocalContext.current
        PermissionsSnackbar(
            messageResId = R.string.notifications_enable_in_settings,
            onActionPerformed = {
                openSystemNotificationSettings(notificationsEnabledChecker, context)
                onPermissionsGrantedChange(false)
            },
            onDismiss = { onPermissionsGrantedChange(false) },
        )
    } else {
        onPermissionsGrantedChange(true)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
private fun AskForPermissionsPostTiramisu(
    onPermissionsGrantedChange: (Boolean) -> Unit,
    permissionsRepository: PermissionsRepository = get(),
    notificationsEnabledChecker: NotificationsEnabledChecker = get(),
    alarmsEnabledChecker: AlarmsEnabledChecker = get(),
) {
    if (notificationsEnabledChecker.areLearningNotificationsEnabled() &&
        alarmsEnabledChecker.areAlarmsEnabled()
    ) {
        onPermissionsGrantedChange(true)
        return
    }

    var state by remember { mutableStateOf(PermissionsUiState.IDLE) }
    var updateState by remember { mutableStateOf(true) }

    val notificationsPermission = rememberPermissionState(
        permission = android.Manifest.permission.POST_NOTIFICATIONS,
        onPermissionResult = { updateState = true }
    )

    if (updateState) {
        LaunchedEffect(true) {
            if (!notificationsEnabledChecker.areLearningNotificationsEnabled()) {
                state = if (permissionsRepository.isNotificationsPermissionRequested()) {
                    if (notificationsPermission.status.shouldShowRationale) {
                        PermissionsUiState.NOTIFICATIONS_RATIONALE_SNACKBAR
                    } else {
                        PermissionsUiState.NOTIFICATIONS_SETTINGS_SNACKBAR
                    }
                } else {
                    PermissionsUiState.NOTIFICATIONS_PERMISSION_REQUEST
                }
            } else if (!alarmsEnabledChecker.areAlarmsEnabled()) {
                state = PermissionsUiState.ALARM_SETTINGS_SNACKBAR
            }
            updateState = false
        }
    }

    AskForPermissionsPostTiramisu(
        state = state,
        onStateChange = { state = it },
        notificationsPermission = notificationsPermission,
        onPermissionsGrantedChange = onPermissionsGrantedChange,
        permissionsRepository = permissionsRepository,
        notificationsEnabledChecker = notificationsEnabledChecker,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun AskForPermissionsPostTiramisu(
    state: PermissionsUiState,
    onStateChange: (PermissionsUiState) -> Unit,
    notificationsPermission: PermissionState,
    onPermissionsGrantedChange: (Boolean) -> Unit,
    permissionsRepository: PermissionsRepository,
    notificationsEnabledChecker: NotificationsEnabledChecker,
) {
    when (state) {
        PermissionsUiState.NOTIFICATIONS_PERMISSION_REQUEST -> {
            LaunchedEffect(true) { notificationsPermission.launchPermissionRequest() }
            permissionsRepository.setNotificationsPermissionRequested()
        }
        PermissionsUiState.NOTIFICATIONS_SETTINGS -> {
            openSystemNotificationSettings(notificationsEnabledChecker, LocalContext.current)
            onPermissionsGrantedChange(false)
        }
        PermissionsUiState.ALARM_SETTINGS -> {
            openSystemAlarmSettings(LocalContext.current)
            onPermissionsGrantedChange(false)
        }
        PermissionsUiState.NOTIFICATIONS_RATIONALE_SNACKBAR -> {
            PermissionsSnackbar(
                messageResId = R.string.notifications_permissions_rationale,
                onActionPerformed = {
                    onStateChange(PermissionsUiState.NOTIFICATIONS_PERMISSION_REQUEST)
                },
                onDismiss = { onPermissionsGrantedChange(false) },
            )
        }
        PermissionsUiState.NOTIFICATIONS_SETTINGS_SNACKBAR -> {
            PermissionsSnackbar(
                messageResId = R.string.notifications_enable_in_settings,
                onActionPerformed = {
                    onStateChange(PermissionsUiState.NOTIFICATIONS_SETTINGS)
                },
                onDismiss = { onPermissionsGrantedChange(false) },
            )
        }
        PermissionsUiState.ALARM_SETTINGS_SNACKBAR -> {
            PermissionsSnackbar(
                messageResId = R.string.alarms_enable_in_settings,
                onActionPerformed = {
                    onStateChange(PermissionsUiState.ALARM_SETTINGS)
                },
                onDismiss = { onPermissionsGrantedChange(false) },
            )
        }
        PermissionsUiState.IDLE ->
            Unit
    }
}

@Composable
private fun PermissionsSnackbar(
    @StringRes messageResId: Int,
    onActionPerformed: () -> Unit,
    onDismiss: () -> Unit,
) {
    val message = LocalContext.current.getString(messageResId)
    val actionLabel = stringResource(Res.string.ok)
    val snackbarHostState = LocalSnackbarHostState.current
    LaunchedEffect(true) {
        val snackbarResult = snackbarHostState.showSnackbar(
            RationaleSnackbarVisuals(message = message, actionLabel = actionLabel)
        )
        when (snackbarResult) {
            SnackbarResult.ActionPerformed -> onActionPerformed()
            SnackbarResult.Dismissed -> onDismiss()
        }
    }
}

private fun openSystemNotificationSettings(
    notificationsEnabledChecker: NotificationsEnabledChecker,
    context: Context,
) {
    if (notificationsEnabledChecker.areNotificationsEnabledForApp() &&
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    ) {
        openSystemNotificationChannelSettings(context)
    } else {
        openSystemAppNotificationSettings(context)
    }
}

private fun openSystemAppNotificationSettings(context: Context) {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
    } else {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${context.packageName}"),
        ).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
        }
    }
    context.startActivity(intent)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun openSystemNotificationChannelSettings(context: Context) {
    val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
    intent.apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        putExtra(Settings.EXTRA_CHANNEL_ID, NotificationChannel.LEARNING.id)
    }
    context.startActivity(intent)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun openSystemAlarmSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
        Uri.parse("package:${context.packageName}")
    )
    context.startActivity(intent)
}

private fun NotificationsEnabledChecker.areLearningNotificationsEnabled(): Boolean {
    return areNotificationsEnabledForChannel(NotificationChannel.LEARNING)
}

private enum class PermissionsUiState {
    IDLE,
    NOTIFICATIONS_PERMISSION_REQUEST,
    NOTIFICATIONS_SETTINGS,
    ALARM_SETTINGS,
    NOTIFICATIONS_RATIONALE_SNACKBAR,
    NOTIFICATIONS_SETTINGS_SNACKBAR,
    ALARM_SETTINGS_SNACKBAR,
}

private class RationaleSnackbarVisuals(
    override val message: String,
    override val actionLabel: String,
) : SnackbarVisuals {

    override val duration = SnackbarDuration.Indefinite
    override val withDismissAction = true
}
