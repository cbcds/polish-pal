package com.cbcds.polishpal.data.model.words

import androidx.compose.runtime.Immutable
import com.cbcds.polishpal.data.model.JavaSerializable

@Immutable
sealed interface MoodForms : JavaSerializable {

    @Immutable
    data class Indicative(
        val pastTense: TenseForms.Past? = null,
        val presentTense: TenseForms.Present? = null,
        val futureTense: TenseForms.Future? = null,
    ) : MoodForms

    @Immutable
    data class Imperative(
        val forms: List<Form>,
    ) : MoodForms

    @Immutable
    data class Conditional(
        val forms: List<Form>,
    ) : MoodForms
}
