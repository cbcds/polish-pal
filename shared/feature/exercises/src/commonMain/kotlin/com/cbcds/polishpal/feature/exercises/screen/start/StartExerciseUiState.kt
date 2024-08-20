package com.cbcds.polishpal.feature.exercises.screen.start

import com.cbcds.polishpal.data.model.exercises.ExerciseSettings

internal sealed interface StartExerciseUiState {

    data object Idle : StartExerciseUiState

    data class Loaded(
        val initialSettings: ExerciseSettings,
    ) : StartExerciseUiState
}
