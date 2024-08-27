package com.cbcds.polishpal.feature.exercises.screen.results

import com.cbcds.polishpal.data.model.words.Verb

internal sealed interface ResultsUiState {

    data object Loading : ResultsUiState

    data class Loaded(
        val overallResult: Float,
        val verbToResult: LinkedHashMap<Verb, Float>,
    ) : ResultsUiState
}
