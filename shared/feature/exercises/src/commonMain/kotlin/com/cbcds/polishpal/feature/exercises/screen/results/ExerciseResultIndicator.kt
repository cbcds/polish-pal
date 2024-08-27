package com.cbcds.polishpal.feature.exercises.screen.results

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.shared.feature.excercises.Res
import com.cbcds.polishpal.shared.feature.excercises.results
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

@Composable
internal fun ExerciseResultIndicator(
    overallResult: Float,
    modifier: Modifier = Modifier,
) {
    var progress by remember { mutableFloatStateOf(0f) }
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
        label = "progress",
    )

    LaunchedEffect(overallResult) {
        progress = overallResult
    }

    Box(modifier) {
        val indicatorColor = lerp(
            start = AppTheme.colorScheme.primary,
            stop = AppTheme.colorScheme.inversePrimary,
            fraction = 0.5f,
        )

        Image(
            painter = painterResource(Res.drawable.results),
            contentDescription = null,
            modifier = Modifier
                .height(300.dp),
        )

        CircularProgressIndicator(
            progress = { progressAnimation },
            color = indicatorColor,
            trackColor = Color.White,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center),
        )

        Text(
            text = "${(overallResult * 100).roundToInt()}%",
            style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = indicatorColor,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
