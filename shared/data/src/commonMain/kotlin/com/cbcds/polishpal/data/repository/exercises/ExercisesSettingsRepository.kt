package com.cbcds.polishpal.data.repository.exercises

import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.data.model.exercises.ExerciseType

interface ExercisesSettingsRepository {

    suspend fun getExerciseSettings(exerciseType: ExerciseType): ExerciseSettings

    fun saveExerciseSettings(settings: ExerciseSettings)

//    suspend fun getIndicativeMoodExerciseSettings(): ExerciseSettings.IndicativeMood
//
//    fun setIndicativeMoodExerciseSettings(settings: ExerciseSettings.IndicativeMood)
//
//    suspend fun getImperativeMoodExerciseSettings(): ExerciseSettings.ImperativeMood
//
//    fun setImperativeMoodExerciseSettings(settings: ExerciseSettings.ImperativeMood)
//
//    suspend fun getConditionalMoodExerciseSettings(): ExerciseSettings.ConditionalMood
//
//    fun setConditionalMoodExerciseSettings(settings: ExerciseSettings.ConditionalMood)
}
