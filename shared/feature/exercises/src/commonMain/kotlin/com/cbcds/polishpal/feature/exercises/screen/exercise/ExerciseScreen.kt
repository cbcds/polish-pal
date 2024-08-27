package com.cbcds.polishpal.feature.exercises.screen.exercise

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.cbcds.polishpal.core.navigation.BackHandler
import com.cbcds.polishpal.core.ui.animation.SlideVerticallyTransition
import com.cbcds.polishpal.data.model.exercises.Exercise
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.feature.exercises.model.FormCheckResult
import com.cbcds.polishpal.feature.exercises.navigation.ResultsScreen
import com.cbcds.polishpal.feature.grammar.component.WordInfoDialog
import com.cbcds.polishpal.shared.feature.excercises.Res
import com.cbcds.polishpal.shared.feature.excercises.info
import kotlinx.collections.immutable.ImmutableMap
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun ExerciseScreen(
    settings: ExerciseSettings,
    viewModel: ExerciseViewModel = koinViewModel(parameters = { parametersOf(settings) }),
) {
    when (val state = viewModel.uiState.collectAsState().value) {
        is ExerciseUiState.Loading ->
            Unit
        is ExerciseUiState.Loaded -> {
            val visibleState = remember { MutableTransitionState(initialState = false) }

            SlideVerticallyTransition(visibleState) {
                ExerciseScreen(
                    state = state,
                    onFavoriteClick = viewModel::onFavoriteClick,
                    onCheckExerciseClick = viewModel::onCheckExerciseClick,
                    onNextExerciseClick = viewModel::onNextExerciseClick,
                )
            }

            LaunchedEffect(true) {
                visibleState.targetState = true
            }
        }
        is ExerciseUiState.Finished ->
            LocalNavigator.current?.replace(ResultsScreen(state.statistics))
    }
}

@Composable
private fun ExerciseScreen(
    state: ExerciseUiState.Loaded,
    onFavoriteClick: () -> Unit,
    onCheckExerciseClick: () -> Unit,
    onNextExerciseClick: () -> Unit,
) {
    var showWordInfo by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    val floatingActionButton: @Composable () -> Unit = when (state) {
        is ExerciseUiState.InProgress -> {
            {
                ExerciseFloatingActionButton(
                    type = ExerciseFloatingActionButtonType.CHECK,
                    onClick = onCheckExerciseClick,
                )
            }
        }
        is ExerciseUiState.Checked -> {
            {
                ExerciseFloatingActionButton(
                    type = ExerciseFloatingActionButtonType.NEXT,
                    onClick = onNextExerciseClick,
                )
            }
        }
    }

    ExerciseScaffold(
        verb = state.verb,
        exercise = state.exercise,
        progress = state.progress,
        expectedToActualForms = state.expectedToActualForms,
        formsCheckResult = (state as? ExerciseUiState.Checked)?.checkResult,
        onBackClick = { showExitDialog = true },
        onInfoClick = { showWordInfo = true },
        onFavoriteClick = onFavoriteClick,
        floatingActionButton = floatingActionButton,
    )

    if (showWordInfo) {
        WordInfoDialog(
            word = state.verb,
            imageRes = Res.drawable.info,
            onDismiss = { showWordInfo = false }
        )
    }

    if (showExitDialog) {
        val navigator = LocalNavigator.current
        ExitExerciseDialog(
            onConfirm = { navigator?.pop() },
            onDismiss = { showExitDialog = false },
        )
    }

    BackHandler { showExitDialog = true }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseScaffold(
    verb: Verb,
    exercise: Exercise,
    progress: Float,
    expectedToActualForms: ImmutableMap<Form, MutableState<String>>,
    formsCheckResult: ImmutableMap<Form, FormCheckResult>?,
    floatingActionButton: @Composable () -> Unit,
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults
        .exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            ExerciseTopAppBar(
                exerciseProgress = progress,
                onBackClick = onBackClick,
                scrollBehavior = scrollBehavior,
            )
        },
        content = { padding ->
            ExerciseContent(
                verb = verb,
                exercise = exercise,
                expectedToActualForms = expectedToActualForms,
                formsCheckResult = formsCheckResult,
                onInfoClick = onInfoClick,
                onFavoriteClick = onFavoriteClick,
                modifier = Modifier.padding(padding),
            )
        },
        floatingActionButton = {
            floatingActionButton()
        },
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    )
}
