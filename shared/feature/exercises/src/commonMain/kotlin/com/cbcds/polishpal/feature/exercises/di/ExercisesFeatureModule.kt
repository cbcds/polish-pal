package com.cbcds.polishpal.feature.exercises.di

import com.cbcds.polishpal.core.kotlin.di.DEFAULT_DISPATCHER
import com.cbcds.polishpal.core.kotlin.di.IO_DISPATCHER
import com.cbcds.polishpal.data.di.dataModule
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.feature.exercises.coordinator.ExerciseChecker
import com.cbcds.polishpal.feature.exercises.coordinator.ExerciseCoordinator
import com.cbcds.polishpal.feature.exercises.screen.exercise.ExerciseViewModel
import com.cbcds.polishpal.feature.exercises.screen.results.ResultsViewModel
import com.cbcds.polishpal.feature.exercises.screen.start.StartExerciseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val exercisesFeatureModule = module {
    includes(dataModule)

    factoryOf(::ExerciseChecker)

    factory { params ->
        ExerciseCoordinator(
            settings = params[0],
            exerciseChecker = get(),
            exerciseRepository = get(),
            ioDispatcher = get(named(IO_DISPATCHER)),
            defaultDispatcher = get(named(DEFAULT_DISPATCHER)),
        )
    }

    viewModelOf(::StartExerciseViewModel)
    viewModel { params ->
        val settings: ExerciseSettings = params[0]
        ExerciseViewModel(
            coordinator = get(parameters = { parametersOf(settings) }),
            exerciseRepository = get(),
        )
    }
    viewModelOf(::ResultsViewModel)
}
