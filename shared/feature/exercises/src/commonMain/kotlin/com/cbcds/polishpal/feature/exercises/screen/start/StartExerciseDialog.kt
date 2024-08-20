package com.cbcds.polishpal.feature.exercises.screen.start

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.data.model.exercises.ExerciseType
import com.cbcds.polishpal.shared.core.ui.cancel
import com.cbcds.polishpal.shared.feature.excercises.Res
import com.cbcds.polishpal.shared.feature.excercises.number_of_words
import com.cbcds.polishpal.shared.feature.excercises.start
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import com.cbcds.polishpal.shared.core.ui.Res as uiRes

@Composable
fun StartExerciseDialog(
    exerciseType: ExerciseType,
    onDismiss: () -> Unit,
) {
    StartExerciseDialogInternal(
        exerciseType = exerciseType,
        onDismiss = onDismiss,
    )
}

@Composable
private fun StartExerciseDialogInternal(
    exerciseType: ExerciseType,
    onDismiss: () -> Unit,
    viewModel: StartExerciseViewModel = koinViewModel(),
) {
    LaunchedEffect(exerciseType) {
        viewModel.setExerciseType(exerciseType)
    }

    when (val state = viewModel.uiState.collectAsState().value) {
        is StartExerciseUiState.Idle -> Unit
        is StartExerciseUiState.Loaded -> {
            val onDismissRequest = {
                viewModel.clearExerciseType()
                onDismiss()
            }
            when (val initialSettings = state.initialSettings) {
                is ExerciseSettings.IndicativeMood -> {
                    StartIndicativeMoodExerciseDialog(
                        initialWordsNumber = initialSettings.wordsNumber,
                        initialSelectedAspects = initialSettings.aspects,
                        initialSelectedTenses = initialSettings.tenses,
                        onConfirm = { settings ->
                            viewModel.saveExerciseSettings(settings)
                        },
                        onDismiss = onDismissRequest,
                    )
                }
                is ExerciseSettings.ImperativeMood -> {
                    StartImperativeMoodExerciseDialog(
                        initialWordsNumber = initialSettings.wordsNumber,
                        onConfirm = { settings ->
                            viewModel.saveExerciseSettings(settings)
                        },
                        onDismiss = onDismissRequest,
                    )
                }
                is ExerciseSettings.ConditionalMood -> {
                    StartConditionalMoodExerciseDialog(
                        initialWordsNumber = initialSettings.wordsNumber,
                        onConfirm = { settings ->
                            viewModel.saveExerciseSettings(settings)
                        },
                        onDismiss = onDismissRequest,
                    )
                }
            }
        }
    }
}

@Composable
internal fun StartExerciseDialog(
    titleRes: StringResource,
    wordsNumberState: WordsNumberState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    moodSettings: (@Composable ColumnScope.() -> Unit)? = null,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(titleRes),
                style = AppTheme.typography.titleLarge,
            )
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                moodSettings?.invoke(this)

                SettingHeader(Res.string.number_of_words)
                WordsNumberSelector(wordsNumberState)
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(Res.string.start))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(uiRes.string.cancel))
            }
        },
    )
}
