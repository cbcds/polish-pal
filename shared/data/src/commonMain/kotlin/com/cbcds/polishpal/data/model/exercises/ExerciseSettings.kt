package com.cbcds.polishpal.data.model.exercises

import androidx.compose.runtime.Immutable
import com.cbcds.polishpal.data.model.JavaSerializable
import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.data.model.words.Tense

@Immutable
sealed interface ExerciseSettings : JavaSerializable {

    val wordsNumber: Int

    @Immutable
    data class IndicativeMood(
        override val wordsNumber: Int,
        val aspects: Set<Aspect>,
        val tenses: Set<Tense>,
    ) : ExerciseSettings

    @Immutable
    data class ImperativeMood(
        override val wordsNumber: Int,
    ) : ExerciseSettings

    @Immutable
    data class ConditionalMood(
        override val wordsNumber: Int,
    ) : ExerciseSettings
}
