package com.cbcds.polishpal.feature.vocabulary.navigation

import androidx.compose.runtime.Composable
import com.cbcds.polishpal.core.navigation.AppScreen
import com.cbcds.polishpal.feature.vocabulary.screen.word.WordScreen

internal data class WordScreen(private val verbId: Int) : AppScreen {

    override val fullscreen = true

    @Composable
    override fun Content() {
        WordScreen(verbId)
    }
}
