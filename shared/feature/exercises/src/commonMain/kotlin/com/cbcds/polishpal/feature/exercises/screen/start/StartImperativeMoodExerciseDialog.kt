package com.cbcds.polishpal.feature.exercises.screen.start

import androidx.compose.runtime.Composable
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.shared.core.grammar.Res
import com.cbcds.polishpal.shared.core.grammar.title_mood_imperative

@Composable
internal fun StartImperativeMoodExerciseDialog(
    initialWordsNumber: Int,
    onConfirm: (ExerciseSettings.ImperativeMood) -> Unit,
    onDismiss: () -> Unit,
) {
    val wordsNumberState = rememberWordsNumberState(initialWordsNumber)

    StartExerciseDialog(
        titleRes = Res.string.title_mood_imperative,
        wordsNumberState = wordsNumberState,
        onConfirm = {
            onConfirm(
                ExerciseSettings.ImperativeMood(wordsNumberState.value)
            )
        },
        onDismiss = onDismiss,
    )
}
