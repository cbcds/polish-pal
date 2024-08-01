package com.cbcds.polishpal.data.model.words

sealed interface Mood {

    data class Indicative(
        val pastTense: Tense.Past? = null,
        val presentTense: Tense.Present? = null,
        val futureTense: Tense.Future? = null,
    ) : Mood

    data class Imperative(
        val forms: List<Form>,
    ) : Mood

    data class Conditional(
        val forms: List<Form>,
    ) : Mood
}
