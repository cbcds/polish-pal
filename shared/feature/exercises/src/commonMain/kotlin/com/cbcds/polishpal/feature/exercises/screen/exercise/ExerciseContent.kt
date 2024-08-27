package com.cbcds.polishpal.feature.exercises.screen.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.data.model.exercises.Exercise
import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.data.model.words.Number
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.feature.exercises.model.FormCheckResult
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ExerciseContent(
    verb: Verb,
    exercise: Exercise,
    expectedToActualForms: ImmutableMap<Form, MutableState<String>>,
    formsCheckResult: ImmutableMap<Form, FormCheckResult>?,
    onInfoClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 56.dp)
            .fillMaxWidth(),
    ) {
        ExerciseHeader(
            verb = verb,
            exercise = exercise,
            onInfoClick = onInfoClick,
            onFavoriteClick = onFavoriteClick,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .padding(top = 32.dp)
                .padding(horizontal = 20.dp),
        ) {
            val groupedForms = when (exercise) {
                is Exercise.IndicativeMood -> {
                    val (singular, plural) = exercise.forms.partition {
                        it.gender.number == Number.SINGULAR
                    }
                    listOf(singular, plural)
                }
                else ->
                    listOf(exercise.forms)
            }

            for (forms in groupedForms) {
                if (forms.isNotEmpty()) {
                    FormsTable(
                        forms = forms.toImmutableList(),
                        expectedToActualForms = expectedToActualForms,
                        formsCheckResult = formsCheckResult,
                    )
                }
            }
        }
    }
}
