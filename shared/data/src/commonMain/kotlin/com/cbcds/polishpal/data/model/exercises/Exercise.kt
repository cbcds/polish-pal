package com.cbcds.polishpal.data.model.exercises

import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.data.model.words.Tense
import kotlinx.collections.immutable.ImmutableList

sealed interface Exercise {

    val verbId: Int
    val forms: ImmutableList<Form>

    data class IndicativeMood(
        override val verbId: Int,
        override val forms: ImmutableList<Form>,
        val tense: Tense,
        val tenseNumber: Int,
        val numberOfTenses: Int,
    ) : Exercise

    data class ImperativeMood(
        override val verbId: Int,
        override val forms: ImmutableList<Form>,
    ) : Exercise

    data class ConditionalMood(
        override val verbId: Int,
        override val forms: ImmutableList<Form>,
    ) : Exercise
}
