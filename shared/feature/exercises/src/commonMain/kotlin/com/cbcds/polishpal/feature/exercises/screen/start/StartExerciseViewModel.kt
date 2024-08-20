package com.cbcds.polishpal.feature.exercises.screen.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.data.model.exercises.ExerciseType
import com.cbcds.polishpal.data.repository.exercises.ExercisesSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class StartExerciseViewModel(
    private val settingsRepository: ExercisesSettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<StartExerciseUiState>(StartExerciseUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun setExerciseType(exerciseType: ExerciseType) {
        viewModelScope.launch {
            _uiState.value = StartExerciseUiState.Loaded(
                settingsRepository.getExerciseSettings(exerciseType)
            )
        }
    }

    // TODO Figure out why VM is not destroyed when the dialog is dismissed
    fun clearExerciseType() {
        _uiState.value = StartExerciseUiState.Idle
    }

    fun saveExerciseSettings(settings: ExerciseSettings) {
        settingsRepository.saveExerciseSettings(settings)
    }
}
