package com.cbcds.polishpal.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.shared.core.ui.cancel
import com.cbcds.polishpal.shared.core.ui.ok
import com.cbcds.polishpal.shared.feature.settings.Res
import com.cbcds.polishpal.shared.feature.settings.time_picker_title
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource
import com.cbcds.polishpal.shared.core.ui.Res as uiRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotificationsTimePicker(
    currentNotificationsTime: LocalTime,
    onNotificationsTimeChange: (LocalTime) -> Unit,
    enabled: Boolean = true,
) {
    var pickerVisible by remember { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState(
        initialHour = currentNotificationsTime.hour,
        initialMinute = currentNotificationsTime.minute,
        is24Hour = true,
    )

    Box {
        SettingsDropDownHeader(
            text = currentNotificationsTime.toString(),
            enabled = enabled,
            onClick = { pickerVisible = true },
        )

        if (pickerVisible) {
            NotificationsTimePicker(
                state = timePickerState,
                onConfirm = {
                    pickerVisible = false
                    val selectedTime = LocalTime(timePickerState.hour, timePickerState.minute)
                    if (selectedTime != currentNotificationsTime) {
                        onNotificationsTimeChange(selectedTime)
                    }
                },
                onDismiss = { pickerVisible = false },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationsTimePicker(
    state: TimePickerState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.time_picker_title),
                style = AppTheme.typography.titleMedium,
            )
        },
        text = { TimePicker(state) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(uiRes.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(uiRes.string.cancel))
            }
        },
    )
}
