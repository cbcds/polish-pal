package com.cbcds.polishpal.feature.exercises.model

internal sealed interface FormCheckResult {

    data class Correct(val otherCorrectForms: List<String>) : FormCheckResult

    data class Typo(
        val formWithoutTypo: String,
        val otherCorrectForms: List<String>,
    ) : FormCheckResult

    sealed interface Error : FormCheckResult {

        val correctForms: List<String>

        data class Incorrect(override val correctForms: List<String>) : Error

        data class Blank(override val correctForms: List<String>) : Error
    }
}
