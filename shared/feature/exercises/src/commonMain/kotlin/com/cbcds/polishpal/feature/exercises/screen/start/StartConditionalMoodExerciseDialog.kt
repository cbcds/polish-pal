package com.cbcds.polishpal.feature.exercises.screen.start

import androidx.compose.runtime.Composable
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.shared.core.grammar.Res
import com.cbcds.polishpal.shared.core.grammar.title_mood_conditional

@Composable
internal fun StartConditionalMoodExerciseDialog(
    initialWordsNumber: Int,
    onConfirm: (ExerciseSettings.ConditionalMood) -> Unit,
    onDismiss: () -> Unit,
) {
    val wordsNumberState = rememberWordsNumberState(initialWordsNumber)

    StartExerciseDialog(
        titleRes = Res.string.title_mood_conditional,
        wordsNumberState = wordsNumberState,
        onConfirm = {
            onConfirm(
                ExerciseSettings.ConditionalMood(wordsNumberState.value)
            )
        },
        onDismiss = onDismiss,
    )
}
