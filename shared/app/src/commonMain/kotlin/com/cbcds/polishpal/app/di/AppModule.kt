package com.cbcds.polishpal.app.di

import com.cbcds.polishpal.app.AppViewModel
import com.cbcds.polishpal.core.kotlin.di.dispatchersModule
import com.cbcds.polishpal.feature.exercises.di.exercisesFeatureModule
import com.cbcds.polishpal.feature.settings.di.settingsFeatureModule
import com.cbcds.polishpal.feature.vocabulary.di.vocabularyFeatureModule
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

fun buildCommonAppModule(globalCoroutineScope: CoroutineScope): Module {
    return module {
        includes(
            dispatchersModule,
            buildCoroutineScopeModule(globalCoroutineScope),
            exercisesFeatureModule,
            settingsFeatureModule,
            vocabularyFeatureModule,
        )

        viewModelOf(::AppViewModel)
    }
}

private fun buildCoroutineScopeModule(coroutineScope: CoroutineScope): Module {
    return module {
        single { coroutineScope }
    }
}
