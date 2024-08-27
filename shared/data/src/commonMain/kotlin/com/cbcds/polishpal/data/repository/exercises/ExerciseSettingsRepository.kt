package com.cbcds.polishpal.data.repository.exercises

import com.cbcds.polishpal.data.model.exercises.ExerciseGroupType
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings

interface ExerciseSettingsRepository {

    suspend fun getExerciseSettings(exerciseGroupType: ExerciseGroupType): ExerciseSettings

    fun saveExerciseSettings(settings: ExerciseSettings)
}
