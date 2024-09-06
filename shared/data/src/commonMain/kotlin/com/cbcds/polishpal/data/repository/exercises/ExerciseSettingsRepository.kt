package com.cbcds.polishpal.data.repository.exercises

import com.cbcds.polishpal.data.model.exercises.ExerciseGroupType
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings

interface ExerciseSettingsRepository {

    suspend fun getSettings(exerciseGroupType: ExerciseGroupType): ExerciseSettings

    fun saveSettings(settings: ExerciseSettings)
}
