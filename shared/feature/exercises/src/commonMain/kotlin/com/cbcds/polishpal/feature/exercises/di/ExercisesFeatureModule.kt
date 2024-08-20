package com.cbcds.polishpal.feature.exercises.di

import com.cbcds.polishpal.data.di.dataModule
import com.cbcds.polishpal.feature.exercises.screen.start.StartExerciseViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val exercisesFeatureModule = module {
    includes(dataModule)

    viewModelOf(::StartExerciseViewModel)
}
