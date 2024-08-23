package com.cbcds.polishpal.feature.vocabulary.screen.words

import com.cbcds.polishpal.data.model.words.Verb

internal sealed interface AllWordsUiEffect {

    data class NavigateToWordScreen(
        val word: Verb,
    ) : AllWordsUiEffect
}
