package com.cbcds.polishpal.feature.settings.di

import com.cbcds.polishpal.data.di.dataModule
import com.cbcds.polishpal.feature.settings.model.AppInfo
import com.cbcds.polishpal.feature.settings.screen.main.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

fun buildSettingsFeatureModule(appInfo: AppInfo): Module {
    return module {
        includes(
            dataModule,
            nativeSettingsFeatureModule,
        )

        factory { appInfo }

        viewModelOf(::SettingsViewModel)
    }
}

internal expect val nativeSettingsFeatureModule: Module
