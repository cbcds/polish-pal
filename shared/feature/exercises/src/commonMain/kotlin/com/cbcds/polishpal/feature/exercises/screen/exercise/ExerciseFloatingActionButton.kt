package com.cbcds.polishpal.feature.exercises.screen.exercise

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.modifier.debounced
import com.cbcds.polishpal.shared.core.ui.Res
import com.cbcds.polishpal.shared.core.ui.ic_checkmark_outline
import com.cbcds.polishpal.shared.core.ui.ic_forward_outline
import org.jetbrains.compose.resources.vectorResource

@Composable
internal fun ExerciseFloatingActionButton(
    type: ExerciseFloatingActionButtonType,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        content = {
            Crossfade(
                targetState = type,
                label = "fab",
            ) { type ->
                val iconRes = when (type) {
                    ExerciseFloatingActionButtonType.CHECK -> Res.drawable.ic_checkmark_outline
                    ExerciseFloatingActionButtonType.NEXT -> Res.drawable.ic_forward_outline
                }

                Icon(
                    imageVector = vectorResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            }
        },
        onClick = debounced(onClick),
    )
}

internal enum class ExerciseFloatingActionButtonType {
    CHECK, NEXT
}
