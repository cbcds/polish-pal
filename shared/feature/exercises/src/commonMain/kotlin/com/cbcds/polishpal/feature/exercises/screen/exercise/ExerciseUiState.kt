package com.cbcds.polishpal.feature.exercises.screen.exercise

import androidx.compose.runtime.MutableState
import com.cbcds.polishpal.data.model.exercises.Exercise
import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.feature.exercises.model.ExerciseGroupStatistics
import com.cbcds.polishpal.feature.exercises.model.FormCheckResult
import kotlinx.collections.immutable.ImmutableMap

internal sealed interface ExerciseUiState {

    data object Loading : ExerciseUiState

    sealed interface Loaded : ExerciseUiState {
        val verb: Verb
        val exercise: Exercise
        val progress: Float
        val expectedToActualForms: ImmutableMap<Form, MutableState<String>>
    }

    data class InProgress(
        override val verb: Verb,
        override val exercise: Exercise,
        override val progress: Float,
        override val expectedToActualForms: ImmutableMap<Form, MutableState<String>>,
    ) : Loaded

    data class Checked(
        override val verb: Verb,
        override val exercise: Exercise,
        override val progress: Float,
        override val expectedToActualForms: ImmutableMap<Form, MutableState<String>>,
        val checkResult: ImmutableMap<Form, FormCheckResult>,
    ) : Loaded

    class Finished(
        val statistics: ExerciseGroupStatistics,
    ) : ExerciseUiState
}
