package com.cbcds.polishpal.feature.vocabulary.navigation

import androidx.compose.runtime.Composable
import com.cbcds.polishpal.core.navigation.AppScreen
import com.cbcds.polishpal.feature.vocabulary.screen.words.AllWordsScreen

object VocabularyScreen : AppScreen {

    override val fullscreen = false

    @Composable
    override fun Content() {
        AllWordsScreen()
    }
}
