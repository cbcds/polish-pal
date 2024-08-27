package com.cbcds.polishpal.feature.exercises.coordinator

import com.cbcds.polishpal.core.kotlin.withoutDiacritics
import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.feature.exercises.model.FormCheckResult

internal class ExerciseChecker {

    fun check(expected: Form, actual: String): FormCheckResult {
        if (actual.isBlank()) {
            return FormCheckResult.Error.Blank(expected.values)
        }

        val actualTrimmedForm = actual.trim()

        var matchingForm: String? = null
        var matchesWithDiacritics: Boolean? = null
        for (expectedForm in expected.values) {
            if (actualTrimmedForm.equals(expectedForm, ignoreCase = true)) {
                matchingForm = expectedForm
                matchesWithDiacritics = true
            } else if (
                actualTrimmedForm.equals(expectedForm.withoutDiacritics(), ignoreCase = true)
            ) {
                matchingForm = expectedForm
                matchesWithDiacritics = false
            }

            if (matchingForm != null) break
        }

        return if (matchingForm != null) {
            if (matchesWithDiacritics == true) {
                FormCheckResult.Correct(
                    otherCorrectForms = expected.values - matchingForm,
                )
            } else {
                FormCheckResult.Typo(
                    formWithoutTypo = matchingForm,
                    otherCorrectForms = expected.values - matchingForm,
                )
            }
        } else {
            FormCheckResult.Error.Incorrect(
                correctForms = expected.values,
            )
        }
    }
}
