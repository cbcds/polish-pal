package com.cbcds.polishpal.feature.exercises.coordinator

import com.cbcds.polishpal.feature.exercises.model.ExerciseGroupStatistics
import com.cbcds.polishpal.feature.exercises.model.ExerciseStatistics

private typealias CorrectToAll = Pair<Int, Int>

internal class ExerciseStatisticsHolder {

    private var exercisesNumber = 0
    private var checkedExercisesNumber = 0

    private var checkedFormsNumber = 0
    private var correctFormsNumber = 0
    private var verbIdToResult = linkedMapOf<Int, CorrectToAll>()

    fun start(exercisesNumber: Int) {
        require(exercisesNumber >= 0) { "exercisesNumber must be non-negative" }

        this.exercisesNumber = exercisesNumber
    }

    fun onExerciseChecked(
        verbId: Int,
        checkedFormsNumber: Int,
        correctFormsNumber: Int,
    ): ExerciseStatistics {
        require(checkedFormsNumber >= 0) { "checkedFormsNumber must be non-negative" }
        require(correctFormsNumber >= 0) { "correctFormsNumber must be non-negative" }

        verbIdToResult[verbId] = verbIdToResult[verbId]
            ?.let { (correct, all) -> correct + correctFormsNumber to all + checkedFormsNumber }
            ?: (correctFormsNumber to checkedFormsNumber)

        this.correctFormsNumber += correctFormsNumber
        this.checkedFormsNumber += checkedFormsNumber

        ++checkedExercisesNumber

        return ExerciseStatistics(
            verbId = verbId,
            result = correctFormsNumber.divOrZero(checkedFormsNumber),
        )
    }

    fun getCurrentProgress(): Float {
        return checkedExercisesNumber.divOrZero(exercisesNumber)
    }

    fun finish(): ExerciseGroupStatistics {
        val overallResult = correctFormsNumber.divOrZero(checkedFormsNumber)

        val verbIdToResult = verbIdToResult.mapValuesTo(linkedMapOf()) { (_, correctToAll) ->
            correctToAll.first.divOrZero(correctToAll.second)
        }

        resetStatistics()

        return ExerciseGroupStatistics(
            overallResult = overallResult,
            verbIdToResult = verbIdToResult,
        )
    }

    private fun resetStatistics() {
        this.exercisesNumber = 0
        checkedExercisesNumber = 0

        checkedFormsNumber = 0
        correctFormsNumber = 0
        verbIdToResult.clear()
    }

    private fun Int.divOrZero(other: Int): Float {
        return if (other != 0) {
            this / other.toFloat()
        } else {
            0f
        }
    }
}
