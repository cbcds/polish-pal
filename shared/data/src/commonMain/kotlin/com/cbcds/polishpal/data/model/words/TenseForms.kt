package com.cbcds.polishpal.data.model.words

import androidx.compose.runtime.Immutable
import com.cbcds.polishpal.data.model.JavaSerializable

@Immutable
sealed interface TenseForms : JavaSerializable {

    val forms: List<Form>

    @Immutable
    data class Past(override val forms: List<Form>) : TenseForms

    @Immutable
    data class Present(override val forms: List<Form>) : TenseForms

    @Immutable
    data class Future(override val forms: List<Form>) : TenseForms
}
