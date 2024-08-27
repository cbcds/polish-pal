package com.cbcds.polishpal.feature.exercises.model

import com.cbcds.polishpal.data.model.JavaSerializable

data class ExerciseStatistics(
    val verbId: Int,
    val result: Float,
) : JavaSerializable
