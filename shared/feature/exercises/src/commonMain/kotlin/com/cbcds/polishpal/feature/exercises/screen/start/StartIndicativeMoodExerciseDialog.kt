package com.cbcds.polishpal.feature.exercises.screen.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.component.CheckableFilterChip
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.data.model.words.Tense
import com.cbcds.polishpal.feature.grammar.toStringResource
import com.cbcds.polishpal.shared.core.grammar.Res
import com.cbcds.polishpal.shared.core.grammar.title_aspect
import com.cbcds.polishpal.shared.core.grammar.title_mood_indicative
import com.cbcds.polishpal.shared.core.grammar.title_tense
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun StartIndicativeMoodExerciseDialog(
    initialWordsNumber: Int,
    initialSelectedAspects: Set<Aspect>,
    initialSelectedTenses: Set<Tense>,
    onConfirm: (ExerciseSettings.IndicativeMood) -> Unit,
    onDismiss: () -> Unit,
) {
    val wordsNumberState = rememberWordsNumberState(initialWordsNumber)

    var selectedAspects by rememberSaveable { mutableStateOf(initialSelectedAspects) }
    var showAspectError by rememberSaveable { mutableStateOf(false) }
    var showAspectErrorAnimation by rememberSaveable { mutableStateOf(false) }

    var selectedTenses by rememberSaveable { mutableStateOf(initialSelectedTenses) }
    var showTensesError by rememberSaveable { mutableStateOf(false) }
    var showTensesErrorAnimation by rememberSaveable { mutableStateOf(false) }

    StartExerciseDialog(
        titleRes = Res.string.title_mood_indicative,
        wordsNumberState = wordsNumberState,
        onConfirm = {
            showAspectError = selectedAspects.isEmpty()
            showAspectErrorAnimation = showAspectError

            showTensesError = selectedTenses.isEmpty()
            showTensesErrorAnimation = showTensesError

            if (!showAspectError && !showTensesError) {
                onConfirm(
                    ExerciseSettings.IndicativeMood(
                        wordsNumber = wordsNumberState.value,
                        aspects = selectedAspects,
                        tenses = selectedTenses,
                    )
                )
            }
        },
        onDismiss = onDismiss,
        moodSettings = {
            SettingHeader(
                titleRes = Res.string.title_aspect,
                showError = showAspectError,
                showErrorAnimation = showAspectErrorAnimation,
                onErrorAnimationEnd = { showAspectErrorAnimation = false },
            )
            AspectFilter(
                selectedAspects = selectedAspects,
                onAspectsChange = { aspects ->
                    selectedAspects = aspects
                    if (Aspect.IMPERFECTIVE !in selectedAspects) {
                        selectedTenses -= Tense.PRESENT
                    }
                },
            )

            SettingHeader(
                titleRes = Res.string.title_tense,
                showError = showTensesError,
                showErrorAnimation = showTensesErrorAnimation,
                onErrorAnimationEnd = { showTensesErrorAnimation = false },
            )
            TenseFilter(
                selectedTenses = selectedTenses,
                onTensesChange = { selectedTenses = it },
                selectedAspects = selectedAspects,
            )
        },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AspectFilter(
    selectedAspects: Set<Aspect>,
    onAspectsChange: (Set<Aspect>) -> Unit,
) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Aspect.entries.forEach { aspect ->
            val selected = aspect in selectedAspects
            CheckableFilterChip(
                selected = selected,
                label = stringResource(aspect.toStringResource()),
                onClick = {
                    if (selected) {
                        onAspectsChange(selectedAspects - aspect)
                    } else {
                        onAspectsChange(selectedAspects + aspect)
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TenseFilter(
    selectedTenses: Set<Tense>,
    onTensesChange: (Set<Tense>) -> Unit,
    selectedAspects: Set<Aspect>,
) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Tense.entries.forEach { tense ->
            val enabled = tense != Tense.PRESENT || Aspect.IMPERFECTIVE in selectedAspects
            val selected = tense in selectedTenses
            CheckableFilterChip(
                selected = selected,
                enabled = enabled,
                label = stringResource(tense.toStringResource()),
                onClick = {
                    if (selected) {
                        onTensesChange(selectedTenses - tense)
                    } else {
                        onTensesChange(selectedTenses + tense)
                    }
                },
            )
        }
    }
}
