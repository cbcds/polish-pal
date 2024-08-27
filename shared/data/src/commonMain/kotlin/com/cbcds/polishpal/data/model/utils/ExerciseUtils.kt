package com.cbcds.polishpal.data.model.utils

import com.cbcds.polishpal.data.model.exercises.Exercise
import com.cbcds.polishpal.data.model.exercises.ExerciseType
import com.cbcds.polishpal.data.model.words.Tense

fun Exercise.toExerciseType(): ExerciseType {
    return when (this) {
        is Exercise.IndicativeMood -> {
            when (tense) {
                Tense.PAST -> ExerciseType.INDICATIVE_MOOD_PAST_TENSE
                Tense.PRESENT -> ExerciseType.INDICATIVE_MOOD_PRESENT_TENSE
                Tense.FUTURE -> ExerciseType.INDICATIVE_MOOD_FUTURE_TENSE
            }
        }
        is Exercise.ImperativeMood ->
            ExerciseType.IMPERATIVE_MOOD
        is Exercise.ConditionalMood ->
            ExerciseType.CONDITIONAL_MOOD
    }
}
