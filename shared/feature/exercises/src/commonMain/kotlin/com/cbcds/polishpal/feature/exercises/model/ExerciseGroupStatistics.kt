package com.cbcds.polishpal.feature.exercises.model

import com.cbcds.polishpal.data.model.JavaSerializable

data class ExerciseGroupStatistics(
    val overallResult: Float,
    val verbIdToResult: LinkedHashMap<Int, Float>,
) : JavaSerializable
