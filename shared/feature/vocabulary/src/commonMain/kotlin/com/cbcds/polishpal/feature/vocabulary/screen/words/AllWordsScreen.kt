package com.cbcds.polishpal.feature.vocabulary.screen.words

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.cbcds.polishpal.core.ui.component.LoadingIndicator
import com.cbcds.polishpal.feature.vocabulary.navigation.WordScreen
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun AllWordsScreen(viewModel: AllWordsViewModel = koinViewModel()) {
    val state = viewModel.uiState.collectAsState().value

    Column {
        WordSearchBar(
            query = viewModel.query,
            onQueryChange = viewModel::onQueryChange,
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
        )

        WordFilters(
            onlyFavorite = viewModel.onlyFavorite,
            onOnlyFavoriteChange = viewModel::onOnlyFavoriteChange,
            aspect = viewModel.aspect,
            onAspectChange = viewModel::onAspectChange,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp),
        )

        when (state) {
            is AllWordsUiState.Loading -> {
                LoadingIndicator(Modifier.fillMaxSize())
            }
            is AllWordsUiState.Loaded -> {
                val navigator = LocalNavigator.current
                WordsList(
                    words = state.words,
                    onWordClick = { verb -> navigator?.push(WordScreen(verb.id)) },
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}
