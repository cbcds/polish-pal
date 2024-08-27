package com.cbcds.polishpal.feature.exercises.screen.exercise

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.data.repository.exercises.ExerciseRepository
import com.cbcds.polishpal.feature.exercises.coordinator.ExerciseCoordinator
import com.cbcds.polishpal.feature.exercises.coordinator.ExerciseFlowState
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class ExerciseViewModel(
    private val coordinator: ExerciseCoordinator,
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExerciseUiState>(ExerciseUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var handleVerbUpdateJob: Job? = null
    private var currentVerb: StateFlow<Verb?> = MutableStateFlow(null)

    private var expectedToActualForms: ImmutableMap<Form, MutableState<String>> = persistentMapOf()

    init {
        viewModelScope.launch {
            coordinator.start()

            coordinator.state
                .map { it.toUiState() }
                .collect { _uiState.value = it }
        }
    }

    fun onFavoriteClick() {
        currentVerb.value?.let { verb ->
            viewModelScope.launch {
                exerciseRepository.setIsVerbFavorite(verb.id, !verb.favorite)
            }
        }
    }

    fun onCheckExerciseClick() {
        viewModelScope.launch {
            coordinator.checkExercise(
                expectedToActualForms = expectedToActualForms
                    .mapValues { (_, formState) -> formState.value },
            )
        }
    }

    fun onNextExerciseClick() {
        coordinator.toNextExerciseOrFinish()
    }

    private suspend fun ExerciseFlowState.toUiState(): ExerciseUiState {
        return when (this) {
            is ExerciseFlowState.Initialized ->
                ExerciseUiState.Loading
            is ExerciseFlowState.Started -> {
                coordinator.toNextExerciseOrFinish()
                ExerciseUiState.Loading
            }
            is ExerciseFlowState.Ready ->
                toReadyUiState()
            is ExerciseFlowState.Finished ->
                ExerciseUiState.Finished(statistics)
        }
    }

    private suspend fun ExerciseFlowState.Ready.toReadyUiState(): ExerciseUiState {
        updateCurrentVerbIfNeeded(exercise.verbId)
        val verb = currentVerb.filterNotNull().firstOrNull() ?: return ExerciseUiState.Loading

        return when (this) {
            is ExerciseFlowState.New -> {
                resetExpectedToActualFormsMap(exercise.forms)

                ExerciseUiState.InProgress(
                    verb = verb,
                    exercise = exercise,
                    progress = progress,
                    expectedToActualForms = expectedToActualForms,
                )
            }
            is ExerciseFlowState.Checked -> {
                mapActualFormsToLowercase()

                ExerciseUiState.Checked(
                    verb = verb,
                    exercise = exercise,
                    progress = progress,
                    expectedToActualForms = expectedToActualForms,
                    checkResult = checkResult,
                )
            }
        }
    }

    private fun updateCurrentVerbIfNeeded(id: Int) {
        if (id != currentVerb.value?.id) {
            currentVerb = exerciseRepository.getVerbInfinitiveWithDefinition(id)
                .stateIn(viewModelScope, SharingStarted.Eagerly, null)

            handleVerbUpdateJob?.cancel()
            handleVerbUpdateJob = viewModelScope.launch {
                currentVerb.collect { verb ->
                    verb?.let(::updateUiStateOnVerbUpdate)
                }
            }
        }
    }

    private fun updateUiStateOnVerbUpdate(verb: Verb) {
        when (val state = _uiState.value) {
            is ExerciseUiState.InProgress -> {
                if (state.verb.id == verb.id) {
                    _uiState.value = state.copy(verb = verb)
                }
            }
            is ExerciseUiState.Checked -> {
                if (state.verb.id == verb.id) {
                    _uiState.value = state.copy(verb = verb)
                }
            }
            else -> Unit
        }
    }

    private fun resetExpectedToActualFormsMap(newForms: List<Form>) {
        expectedToActualForms = newForms
            .associateWith { mutableStateOf("") }
            .toImmutableMap()
    }

    private fun mapActualFormsToLowercase() {
        expectedToActualForms.forEach { (_, form) ->
            form.value = form.value.lowercase()
        }
    }
}
