package com.cbcds.polishpal.feature.vocabulary.screen.word

import com.cbcds.polishpal.data.model.words.Verb

internal sealed interface WordUiState {

    data object Loading : WordUiState

    data class Loaded(
        val word: Verb,
    ) : WordUiState
}
