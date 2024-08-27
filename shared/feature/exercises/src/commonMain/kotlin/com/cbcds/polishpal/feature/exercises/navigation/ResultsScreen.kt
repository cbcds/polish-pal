package com.cbcds.polishpal.feature.exercises.navigation

import androidx.compose.runtime.Composable
import com.cbcds.polishpal.core.navigation.AppScreen
import com.cbcds.polishpal.feature.exercises.model.ExerciseGroupStatistics
import com.cbcds.polishpal.feature.exercises.screen.results.ResultsScreen

data class ResultsScreen(val statistics: ExerciseGroupStatistics) : AppScreen {

    override val fullscreen = true

    @Composable
    override fun Content() {
        ResultsScreen(statistics)
    }
}
