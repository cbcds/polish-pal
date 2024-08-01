package com.cbcds.polishpal.feature.vocabulary.screen.words

import com.cbcds.polishpal.data.model.words.Verb

internal sealed interface AllWordsUiState {

    data object Loading : AllWordsUiState

    data class Loaded(
        val words: List<Verb>,
    ) : AllWordsUiState
}
