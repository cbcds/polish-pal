package com.cbcds.polishpal.feature.exercises.screen.exercise

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.theme.AppTheme

@Composable
internal fun ExerciseProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "progress",
    )

    LinearProgressIndicator(
        progress = { progressAnimation },
        color = AppTheme.extendedColorScheme.accent1.colorContainer,
        trackColor = AppTheme.colorScheme.surfaceContainerLowest,
        strokeCap = StrokeCap.Round,
        gapSize = 0.dp,
        drawStopIndicator = {},
        modifier = modifier.height(16.dp),
    )
}
