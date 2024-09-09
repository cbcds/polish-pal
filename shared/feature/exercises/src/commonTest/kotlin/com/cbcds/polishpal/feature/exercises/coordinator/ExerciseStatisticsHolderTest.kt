package com.cbcds.polishpal.feature.exercises.coordinator

import com.cbcds.polishpal.feature.exercises.model.ExerciseGroupStatistics
import com.cbcds.polishpal.feature.exercises.model.ExerciseStatistics
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ExerciseStatisticsHolderTest {

    @Test
    fun `should fail when negative number of exercises is passed`() {
        val statisticsHolder = ExerciseStatisticsHolder()

        assertFails {
            statisticsHolder.start(exercisesNumber = -4)
        }
    }

    @Test
    fun `should fail when negative number of checked forms is passed`() {
        val statisticsHolder = ExerciseStatisticsHolder()

        statisticsHolder.start(exercisesNumber = 1)

        assertFails {
            statisticsHolder.onExerciseChecked(
                verbId = 0,
                checkedFormsNumber = -3,
                correctFormsNumber = 1,
            )
        }
    }

    @Test
    fun `should fail when negative number of correct forms is passed`() {
        val statisticsHolder = ExerciseStatisticsHolder()

        statisticsHolder.start(exercisesNumber = 1)

        assertFails {
            statisticsHolder.onExerciseChecked(
                verbId = 0,
                checkedFormsNumber = 3,
                correctFormsNumber = -1,
            )
        }
    }

    @Test
    fun `should correctly calculate current progress`() {
        val statisticsHolder = ExerciseStatisticsHolder()

        val exercisesNum = 10
        val checkedExercisesNum = 5
        statisticsHolder.start(exercisesNumber = exercisesNum)
        repeat(checkedExercisesNum) {
            statisticsHolder.onExerciseChecked(
                verbId = 0,
                checkedFormsNumber = 3,
                correctFormsNumber = 3,
            )
        }

        val expectedProgress = checkedExercisesNum / exercisesNum.toFloat()

        assertEquals(expectedProgress, statisticsHolder.getCurrentProgress())
    }

    @Test
    fun `should correctly calculate statistics for current exercise`() {
        val statisticsHolder = ExerciseStatisticsHolder()

        statisticsHolder.start(exercisesNumber = 1)

        val checkedFormsNum = 10
        val correctFormsNum = 5
        val actualStats = statisticsHolder.onExerciseChecked(
            verbId = 0,
            checkedFormsNumber = checkedFormsNum,
            correctFormsNumber = correctFormsNum,
        )

        val expectedStats = ExerciseStatistics(
            verbId = 0,
            result = correctFormsNum / checkedFormsNum.toFloat()
        )

        assertEquals(expectedStats, actualStats)
    }

    @Test
    fun `should correctly calculate statistics for all exercises`() {
        val statisticsHolder = ExerciseStatisticsHolder()

        statisticsHolder.start(exercisesNumber = 2)

        val firstVerbCheckedFormsNum = listOf(5, 6)
        val firstVerbCorrectFormsNum = listOf(2, 3)
        repeat(2) { i ->
            statisticsHolder.onExerciseChecked(
                verbId = 0,
                checkedFormsNumber = firstVerbCheckedFormsNum[i],
                correctFormsNumber = firstVerbCorrectFormsNum[i],
            )
        }

        val secondVerbCheckedFormsNum = listOf(10, 11)
        val secondVerbCorrectFormsNum = listOf(4, 5)
        repeat(2) { i ->
            statisticsHolder.onExerciseChecked(
                verbId = 1,
                checkedFormsNumber = secondVerbCheckedFormsNum[i],
                correctFormsNumber = secondVerbCorrectFormsNum[i],
            )
        }

        val actualStats = statisticsHolder.finish()

        val expectedOverallResult =
            (firstVerbCorrectFormsNum.sum() + secondVerbCorrectFormsNum.sum()) /
                (firstVerbCheckedFormsNum.sum() + secondVerbCheckedFormsNum.sum()).toFloat()
        val expectedVerbIdToResult = linkedMapOf(
            0 to firstVerbCorrectFormsNum.sum() / firstVerbCheckedFormsNum.sum().toFloat(),
            1 to secondVerbCorrectFormsNum.sum() / secondVerbCheckedFormsNum.sum().toFloat(),
        )
        val expectedStats = ExerciseGroupStatistics(
            overallResult = expectedOverallResult,
            verbIdToResult = expectedVerbIdToResult,
        )

        assertEquals(expectedStats, actualStats)
    }

    @Test
    fun `should reset statistics after finish() is called`() {
        val statisticsHolder = ExerciseStatisticsHolder()

        statisticsHolder.start(exercisesNumber = 1)
        statisticsHolder.onExerciseChecked(
            verbId = 0,
            checkedFormsNumber = 10,
            correctFormsNumber = 5,
        )
        statisticsHolder.finish()

        val actualStats = statisticsHolder.finish()

        val expectedStats = ExerciseGroupStatistics(
            overallResult = 0f,
            verbIdToResult = linkedMapOf(),
        )

        assertEquals(0f, statisticsHolder.getCurrentProgress())
        assertEquals(expectedStats, actualStats)
    }
}
