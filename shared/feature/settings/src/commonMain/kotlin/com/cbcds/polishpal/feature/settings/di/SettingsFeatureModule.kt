package com.cbcds.polishpal.feature.settings.di

import com.cbcds.polishpal.data.di.dataModule
import com.cbcds.polishpal.feature.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

val settingsFeatureModule = module {
    includes(
        dataModule,
        nativeSettingsFeatureModule,
    )

    viewModelOf(::SettingsViewModel)
}

internal expect val nativeSettingsFeatureModule: Module
