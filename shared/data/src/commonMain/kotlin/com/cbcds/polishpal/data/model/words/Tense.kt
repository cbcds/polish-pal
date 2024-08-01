package com.cbcds.polishpal.data.model.words

sealed interface Tense {

    val forms: List<Form>

    data class Past(override val forms: List<Form>) : Tense

    data class Present(override val forms: List<Form>) : Tense

    data class Future(override val forms: List<Form>) : Tense
}
