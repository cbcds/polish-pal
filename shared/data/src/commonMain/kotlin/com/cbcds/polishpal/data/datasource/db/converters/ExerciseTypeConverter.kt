package com.cbcds.polishpal.data.datasource.db.converters

import androidx.room.TypeConverter
import com.cbcds.polishpal.data.model.exercises.ExerciseType

internal class ExerciseTypeConverter {

    @TypeConverter
    fun toExerciseType(type: String): ExerciseType? {
        return when (type) {
            INDICATIVE_MOOD_PAST_TENSE -> ExerciseType.INDICATIVE_MOOD_PAST_TENSE
            INDICATIVE_MOOD_PRESENT_TENSE -> ExerciseType.INDICATIVE_MOOD_PRESENT_TENSE
            INDICATIVE_MOOD_FUTURE_TENSE -> ExerciseType.INDICATIVE_MOOD_FUTURE_TENSE
            IMPERATIVE_MOOD -> ExerciseType.IMPERATIVE_MOOD
            CONDITIONAL_MOOD -> ExerciseType.CONDITIONAL_MOOD
            else -> null
        }
    }

    @TypeConverter
    fun toString(type: ExerciseType): String {
        return when (type) {
            ExerciseType.INDICATIVE_MOOD_PAST_TENSE -> INDICATIVE_MOOD_PAST_TENSE
            ExerciseType.INDICATIVE_MOOD_PRESENT_TENSE -> INDICATIVE_MOOD_PRESENT_TENSE
            ExerciseType.INDICATIVE_MOOD_FUTURE_TENSE -> INDICATIVE_MOOD_FUTURE_TENSE
            ExerciseType.IMPERATIVE_MOOD -> IMPERATIVE_MOOD
            ExerciseType.CONDITIONAL_MOOD -> CONDITIONAL_MOOD
        }
    }

    companion object {

        const val INDICATIVE_MOOD_PAST_TENSE = "ind_past"
        const val INDICATIVE_MOOD_PRESENT_TENSE = "ind_pres"
        const val INDICATIVE_MOOD_FUTURE_TENSE = "ind_fut"
        const val IMPERATIVE_MOOD = "imp"
        const val CONDITIONAL_MOOD = "cond"
    }
}
