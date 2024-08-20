package com.cbcds.polishpal.data.model.exercises

import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.data.model.words.Tense

sealed interface ExerciseSettings {

    val wordsNumber: Int

    data class IndicativeMood(
        override val wordsNumber: Int,
        val aspects: Set<Aspect>,
        val tenses: Set<Tense>,
    ) : ExerciseSettings

    data class ImperativeMood(
        override val wordsNumber: Int,
    ) : ExerciseSettings

    data class ConditionalMood(
        override val wordsNumber: Int,
    ) : ExerciseSettings
}
