package com.cbcds.polishpal.feature.exercises.screen.exercise

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.component.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExerciseTopAppBar(
    exerciseProgress: Float,
    onBackClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    TopAppBar(
        title = {
            ExerciseProgressIndicator(
                progress = exerciseProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
            )
        },
        onBackClick = onBackClick,
        scrollBehavior = scrollBehavior,
    )
}
