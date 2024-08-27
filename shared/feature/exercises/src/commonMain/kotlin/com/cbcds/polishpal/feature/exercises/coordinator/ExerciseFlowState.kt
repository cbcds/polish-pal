package com.cbcds.polishpal.feature.exercises.coordinator

import com.cbcds.polishpal.data.model.exercises.Exercise
import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.feature.exercises.model.ExerciseGroupStatistics
import com.cbcds.polishpal.feature.exercises.model.FormCheckResult
import kotlinx.collections.immutable.ImmutableMap

internal sealed interface ExerciseFlowState {

    data object Initialized : ExerciseFlowState

    data object Started : ExerciseFlowState

    sealed interface Ready : ExerciseFlowState {

        val exercise: Exercise
        val progress: Float
    }

    data class New(
        override val exercise: Exercise,
        override val progress: Float,
    ) : Ready

    data class Checked(
        override val exercise: Exercise,
        override val progress: Float,
        val checkResult: ImmutableMap<Form, FormCheckResult>,
    ) : Ready

    data class Finished(
        val statistics: ExerciseGroupStatistics,
    ) : ExerciseFlowState
}
