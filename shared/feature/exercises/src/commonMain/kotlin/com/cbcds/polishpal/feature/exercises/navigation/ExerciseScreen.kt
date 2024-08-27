package com.cbcds.polishpal.feature.exercises.navigation

import androidx.compose.runtime.Composable
import com.cbcds.polishpal.core.navigation.AppScreen
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.feature.exercises.screen.exercise.ExerciseScreen

data class ExerciseScreen(val settings: ExerciseSettings) : AppScreen {

    override val fullscreen = true

    @Composable
    override fun Content() {
        ExerciseScreen(settings)
    }
}
