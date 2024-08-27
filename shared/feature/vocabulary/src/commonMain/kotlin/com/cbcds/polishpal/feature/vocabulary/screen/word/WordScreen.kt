package com.cbcds.polishpal.feature.vocabulary.screen.word

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.cbcds.polishpal.data.model.words.Mood
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.feature.grammar.component.WordInfoDialog
import com.cbcds.polishpal.shared.feature.vocabulary.Res
import com.cbcds.polishpal.shared.feature.vocabulary.reading
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WordScreen(
    verb: Verb,
    viewModel: WordViewModel = koinViewModel(parameters = { parametersOf(verb) }),
) {
    val state = viewModel.uiState.collectAsState().value
    var showWordInfo by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults
        .exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            val navigator = LocalNavigator.current
            WordTopAppBar(
                word = state.word,
                onInfoClick = { showWordInfo = true },
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

    if (showWordInfo) {
        WordInfoDialog(
            word = state.word,
            imageRes = Res.drawable.reading,
            onDismiss = { showWordInfo = false }
        )
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
