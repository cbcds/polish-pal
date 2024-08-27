package com.cbcds.polishpal.feature.exercises.coordinator

import com.cbcds.polishpal.data.model.exercises.Exercise
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.data.model.exercises.ExerciseType
import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.data.model.words.Tense
import com.cbcds.polishpal.data.model.words.Verb
import kotlinx.collections.immutable.toImmutableList

private typealias VerbIndexToExerciseType = Pair<Int, ExerciseType>

internal class ExerciseIterator(
    private val verbs: List<Verb>,
    private val settings: ExerciseSettings,
) : AbstractIterator<Exercise>() {

    private val imperfectiveAspectTensesOrder by lazy { computeImperfectiveAspectTensesOrder() }
    private val perfectiveAspectTensesOrder by lazy { computePerfectiveAspectTensesOrder() }

    private val exercisesOrder by lazy { computeExercisesOrder() }
    private var nextExerciseIndex = 0

    val exerciseCount
        get() = exercisesOrder.size

    override fun computeNext() {
        if (nextExerciseIndex < exercisesOrder.size) {
            val (verbIndex, exerciseType) = exercisesOrder[nextExerciseIndex]
            val verb = verbs[verbIndex]
            when (exerciseType) {
                ExerciseType.INDICATIVE_MOOD_PAST_TENSE ->
                    verb.setNextIndicativeMoodExercise(Tense.PAST)
                ExerciseType.INDICATIVE_MOOD_PRESENT_TENSE ->
                    verb.setNextIndicativeMoodExercise(Tense.PRESENT)
                ExerciseType.INDICATIVE_MOOD_FUTURE_TENSE ->
                    verb.setNextIndicativeMoodExercise(Tense.FUTURE)
                ExerciseType.IMPERATIVE_MOOD ->
                    verb.setNextImperativeMoodExercise()
                ExerciseType.CONDITIONAL_MOOD ->
                    verb.setNextConditionalMoodExercise()
            }
            ++nextExerciseIndex
        } else {
            done()
        }
    }

    private fun computeExercisesOrder(): List<VerbIndexToExerciseType> {
        val exercisesOrder = mutableListOf<VerbIndexToExerciseType>()

        var nextVerbIndex = 0
        var nextTenseIndex = 0

        while (nextVerbIndex < verbs.size) {
            val verb = verbs[nextVerbIndex]
            when (settings) {
                is ExerciseSettings.IndicativeMood -> {
                    val tenses = verb.aspect.getTensesOrder()

                    val exerciseType = tenses.getOrNull(nextTenseIndex)
                        ?.let { verb.getIndicativeMoodExerciseType(it) }

                    exerciseType?.let {
                        exercisesOrder.add(nextVerbIndex to exerciseType)
                    }

                    if (nextTenseIndex > tenses.lastIndex) {
                        ++nextVerbIndex
                        nextTenseIndex = 0
                    } else {
                        ++nextTenseIndex
                    }
                }
                is ExerciseSettings.ImperativeMood -> {
                    verb.imperativeMood?.let {
                        exercisesOrder.add(nextVerbIndex to ExerciseType.IMPERATIVE_MOOD)
                    }
                    ++nextVerbIndex
                }
                is ExerciseSettings.ConditionalMood -> {
                    verb.conditionalMood?.let {
                        exercisesOrder.add(nextVerbIndex to ExerciseType.CONDITIONAL_MOOD)
                    }
                    ++nextVerbIndex
                }
            }
        }

        return exercisesOrder
    }

    private fun Verb.getIndicativeMoodExerciseType(tense: Tense): ExerciseType? {
        return when {
            tense == Tense.PAST && indicativeMood?.pastTense?.forms != null ->
                ExerciseType.INDICATIVE_MOOD_PAST_TENSE
            tense == Tense.PRESENT && indicativeMood?.presentTense?.forms != null ->
                ExerciseType.INDICATIVE_MOOD_PRESENT_TENSE
            tense == Tense.FUTURE && indicativeMood?.futureTense?.forms != null ->
                ExerciseType.INDICATIVE_MOOD_FUTURE_TENSE
            else -> null
        }
    }

    private fun Verb.setNextIndicativeMoodExercise(tense: Tense) {
        val forms = when (tense) {
            Tense.PAST -> indicativeMood?.pastTense?.forms
            Tense.PRESENT -> indicativeMood?.presentTense?.forms
            Tense.FUTURE -> indicativeMood?.futureTense?.forms
        }

        forms?.let {
            val allTenses = aspect.getTensesOrder()

            setNext(
                Exercise.IndicativeMood(
                    verbId = id,
                    forms = forms.toImmutableList(),
                    tense = tense,
                    tenseNumber = allTenses.indexOf(tense).coerceAtLeast(1),
                    numberOfTenses = allTenses.size,
                )
            )
        }
    }

    private fun Verb.setNextImperativeMoodExercise() {
        imperativeMood?.let {
            setNext(
                Exercise.ImperativeMood(
                    verbId = id,
                    forms = it.forms.toImmutableList(),
                )
            )
        }
    }

    private fun Verb.setNextConditionalMoodExercise() {
        conditionalMood?.let {
            setNext(
                Exercise.ConditionalMood(
                    verbId = id,
                    forms = it.forms.toImmutableList(),
                )
            )
        }
    }

    private fun computeImperfectiveAspectTensesOrder(): List<Tense> {
        return if (settings is ExerciseSettings.IndicativeMood) {
            listOf(Tense.PRESENT, Tense.PAST, Tense.FUTURE).filter { it in settings.tenses }
        } else {
            emptyList()
        }
    }

    private fun computePerfectiveAspectTensesOrder(): List<Tense> {
        return if (settings is ExerciseSettings.IndicativeMood) {
            listOf(Tense.FUTURE, Tense.PAST).filter { it in settings.tenses }
        } else {
            emptyList()
        }
    }

    private fun Aspect.getTensesOrder(): List<Tense> {
        return when (this) {
            Aspect.PERFECTIVE -> perfectiveAspectTensesOrder
            Aspect.IMPERFECTIVE -> imperfectiveAspectTensesOrder
        }
    }
}
