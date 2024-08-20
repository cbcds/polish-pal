package com.cbcds.polishpal.feature.vocabulary.screen.word

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.data.model.words.Mood
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.shared.core.ui.ok
import com.cbcds.polishpal.shared.feature.vocabulary.Res
import com.cbcds.polishpal.shared.feature.vocabulary.reading
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import com.cbcds.polishpal.shared.core.ui.Res as uiRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WordScreen(
    verbId: Int,
    viewModel: WordViewModel = koinViewModel()
) {
    LaunchedEffect(verbId) {
        viewModel.setCurrentVerbId(verbId)
    }

    val state = viewModel.uiState.collectAsState().value
    val navigator = LocalNavigator.current

    when (state) {
        is WordUiState.Loading -> Unit
        is WordUiState.Loaded -> {
            val scrollBehavior = TopAppBarDefaults
                .exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
            var wordInfoVisible by remember { mutableStateOf(false) }

            Scaffold(
                topBar = {
                    WordTopAppBar(
                        word = state.word,
                        onInfoClick = { wordInfoVisible = true },
                        onFavoriteClick = viewModel::onFavoriteClick,
                        onBackClick = { navigator?.pop() },
                        scrollBehavior = scrollBehavior,
                    )
                },
                content = { padding ->
                    WordForms(
                        word = state.word,
                        modifier = Modifier.padding(padding),
                    )
                },
                contentWindowInsets = WindowInsets(0.dp),
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            )

            if (wordInfoVisible) {
                WordInfoDialog(
                    word = state.word,
                    onDismiss = { wordInfoVisible = false }
                )
            }
        }
    }
}

@Composable
private fun WordForms(
    word: Verb,
    modifier: Modifier = Modifier,
) {
    val tabsData = word.getMoodTabsData()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { Mood.entries.size })

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier.fillMaxSize(),
    ) {
        MoodPager(
            tabsData = tabsData,
            pagerState = pagerState,
        )
        MoodTabs(
            tabsData = tabsData,
            pagerState = pagerState,
        )
    }
}

@Composable
private fun WordInfoDialog(
    word: Verb,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = word.infinitive,
                style = AppTheme.typography.titleLarge,
            )
        },
        text = {
            Column {
                SelectionContainer {
                    Text(
                        text = word.definition.orEmpty(),
                        style = AppTheme.typography.bodyMedium,
                    )
                }
                Image(
                    painter = painterResource(Res.drawable.reading),
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = (-24).dp, y = 20.dp)
                        .height(132.dp),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(uiRes.string.ok))
            }
        },
        dismissButton = null,
    )
}
