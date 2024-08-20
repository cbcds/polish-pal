package com.cbcds.polishpal.data.model.words

import kotlinx.collections.immutable.ImmutableList

sealed interface TenseForms {

    val forms: ImmutableList<Form>

    data class Past(override val forms: ImmutableList<Form>) : TenseForms

    data class Present(override val forms: ImmutableList<Form>) : TenseForms

    data class Future(override val forms: ImmutableList<Form>) : TenseForms
}
