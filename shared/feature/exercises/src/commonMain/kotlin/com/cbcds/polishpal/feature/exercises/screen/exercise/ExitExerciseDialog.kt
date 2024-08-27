package com.cbcds.polishpal.feature.exercises.screen.exercise

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.shared.core.ui.cancel
import com.cbcds.polishpal.shared.core.ui.yes
import com.cbcds.polishpal.shared.feature.excercises.Res
import com.cbcds.polishpal.shared.feature.excercises.quit_dialog
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ExitExerciseDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Text(
                text = stringResource(Res.string.quit_dialog),
                color = AppTheme.colorScheme.onSurface,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(com.cbcds.polishpal.shared.core.ui.Res.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(com.cbcds.polishpal.shared.core.ui.Res.string.cancel))
            }
        },
    )
}
