package com.cbcds.polishpal.data.model.words

import kotlinx.collections.immutable.ImmutableList

sealed interface MoodForms {

    data class Indicative(
        val pastTense: TenseForms.Past? = null,
        val presentTense: TenseForms.Present? = null,
        val futureTense: TenseForms.Future? = null,
    ) : MoodForms

    data class Imperative(
        val forms: ImmutableList<Form>,
    ) : MoodForms

    data class Conditional(
        val forms: ImmutableList<Form>,
    ) : MoodForms
}
