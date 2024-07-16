package com.cbcds.polishpal.data.di

import com.cbcds.polishpal.core.kotlin.di.IO_DISPATCHER
import com.cbcds.polishpal.data.datasource.prefs.DataStorePreferences
import com.cbcds.polishpal.data.datasource.prefs.Preferences
import com.cbcds.polishpal.data.repository.AppSettingsRepository
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    includes(nativeDataModule)

    factory<Preferences> {
        DataStorePreferences(dataStoreProvider = get(), ioDispatcher = get(named(IO_DISPATCHER)))
    }

    single(createdAtStart = true) {
        AppSettingsRepository(
            preferences = get(),
            localeProvider = get(),
            externalScope = get(),
            ioDispatcher = get(named(IO_DISPATCHER)),
        )
    }
}

internal expect val nativeDataModule: Module
