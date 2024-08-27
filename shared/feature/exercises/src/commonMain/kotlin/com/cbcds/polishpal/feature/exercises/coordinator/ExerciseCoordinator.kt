package com.cbcds.polishpal.feature.exercises.coordinator

import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.data.repository.exercises.ExerciseRepository
import com.cbcds.polishpal.feature.exercises.model.FormCheckResult
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

internal class ExerciseCoordinator(
    private val settings: ExerciseSettings,
    private val exerciseChecker: ExerciseChecker,
    private val exerciseRepository: ExerciseRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher,
) {

    private val _state = MutableStateFlow<ExerciseFlowState>(ExerciseFlowState.Initialized)
    val state: StateFlow<ExerciseFlowState> = _state.asStateFlow()

    private var exerciseIterator: ExerciseIterator? = null

    private var statisticsHolder = ExerciseStatisticsHolder()

    suspend fun start() {
        val verbs = exerciseRepository.getVerbs(settings)

        val iterator = ExerciseIterator(verbs, settings)
        exerciseIterator = iterator

        statisticsHolder.start(iterator.exerciseCount)

        _state.value = ExerciseFlowState.Started
    }

    fun toNextExerciseOrFinish() {
        val iterator = exerciseIterator
            ?: error("ExerciseIterator is not initialized yet, did you forget to call start()?")

        _state.value = if (iterator.hasNext()) {
            val exercise = iterator.next()
            ExerciseFlowState.New(
                exercise = exercise,
                progress = statisticsHolder.getCurrentProgress(),
            )
        } else {
            ExerciseFlowState.Finished(statistics = statisticsHolder.getOverallStatistics())
        }
    }

    suspend fun checkExercise(
        expectedToActualForms: Map<Form, String>,
    ) {
        val currentState = _state.value

        if (currentState !is ExerciseFlowState.New) {
            error("State must be ExercisesState.New, but was $currentState")
        }

        val exercise = currentState.exercise
        val actualForms = exercise.forms.toSet()
        if (actualForms != expectedToActualForms.keys) {
            error(
                "Forms ${expectedToActualForms.keys} differ from " +
                    "current exercise forms ${exercise.forms}"
            )
        }

        val checkResult = withContext(defaultDispatcher) {
            expectedToActualForms.mapValues { (expected, actual) ->
                exerciseChecker.check(expected, actual)
            }.toImmutableMap()
        }

        val stats = statisticsHolder.onExerciseChecked(
            verbId = currentState.exercise.verbId,
            checkedFormsNumber = expectedToActualForms.size,
            correctFormsNumber = checkResult.count { (_, result) ->
                result is FormCheckResult.Correct || result is FormCheckResult.Typo
            },
        )

        CoroutineScope(coroutineContext + ioDispatcher).launch {
            exerciseRepository.saveResult(exercise, stats.result)
        }

        _state.value = ExerciseFlowState.Checked(
            exercise = exercise,
            checkResult = checkResult,
            progress = statisticsHolder.getCurrentProgress(),
        )
    }
}
