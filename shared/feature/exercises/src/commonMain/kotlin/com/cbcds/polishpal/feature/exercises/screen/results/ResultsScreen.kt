package com.cbcds.polishpal.feature.exercises.screen.results

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.cbcds.polishpal.core.ui.animation.SlideHorizontallyTransition
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.feature.exercises.model.ExerciseGroupStatistics
import com.cbcds.polishpal.shared.core.ui.Res
import com.cbcds.polishpal.shared.core.ui.ic_checkmark_circle_outline
import org.jetbrains.compose.resources.vectorResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun ResultsScreen(
    statistics: ExerciseGroupStatistics,
    viewModel: ResultsViewModel = koinViewModel(parameters = { parametersOf(statistics) }),
) {
    when (val state = viewModel.uiState.collectAsState().value) {
        is ResultsUiState.Loading -> Unit
        is ResultsUiState.Loaded -> {
            val visibleState = remember { MutableTransitionState(initialState = false) }

            SlideHorizontallyTransition(visibleState) {
                ResultsScaffold(
                    state = state,
                    onFavoriteClick = viewModel::onFavoriteClick,
                )
            }

            LaunchedEffect(true) {
                visibleState.targetState = true
            }
        }
    }
}

@Composable
private fun ResultsScaffold(
    state: ResultsUiState.Loaded,
    onFavoriteClick: (verbId: Int) -> Unit,
) {
    Scaffold(
        content = { padding ->
            ResultContent(
                state = state,
                onFavoriteClick = onFavoriteClick,
                modifier = Modifier.padding(padding),
            )
        },
        floatingActionButton = {
            val navigator = LocalNavigator.current
            FloatingActionButton(
                onClick = { navigator?.pop() }
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
    )
}

@Composable
private fun ResultContent(
    state: ResultsUiState.Loaded,
    onFavoriteClick: (verbId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
    ) {
        Text(
            text = "Twój wynik",
            style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(top = 28.dp),
        )

        ExerciseResultIndicator(
            overallResult = state.overallResult,
            modifier = Modifier.padding(top = 32.dp),
        )

        ResultsList(
            verbToResult = state.verbToResult,
            onFavoriteClick = onFavoriteClick,
            modifier = modifier
                .padding(top = 28.dp, bottom = 56.dp)
                .padding(horizontal = 20.dp),
        )
    }
}

@Composable
private fun FloatingActionButton(
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        content = {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_checkmark_circle_outline),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
            Text(
                text = "Skończyć",
                modifier = Modifier.padding(start = 12.dp),
            )
        },
        onClick = onClick,
    )
}
