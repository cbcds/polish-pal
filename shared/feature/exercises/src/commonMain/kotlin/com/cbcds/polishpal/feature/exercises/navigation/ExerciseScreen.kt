package com.cbcds.polishpal.feature.exercises.navigation

import androidx.compose.runtime.Composable
import com.cbcds.polishpal.core.navigation.AppScreen
import com.cbcds.polishpal.data.model.exercises.ExerciseType

internal data class ExerciseScreen(private val exerciseType: ExerciseType) : AppScreen {

    override val fullscreen = false

    @Composable
    override fun Content() {
//        StartExerciseDialog(exerciseType)
    }
}
