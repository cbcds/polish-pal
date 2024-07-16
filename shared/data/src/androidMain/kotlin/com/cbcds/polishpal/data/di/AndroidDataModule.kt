package com.cbcds.polishpal.data.di

import com.cbcds.polishpal.data.datasource.AndroidLocaleProvider
import com.cbcds.polishpal.data.datasource.LocaleProvider
import com.cbcds.polishpal.data.datasource.prefs.AndroidDataStoreProvider
import com.cbcds.polishpal.data.datasource.prefs.DataStoreProvider
import com.cbcds.polishpal.data.repository.PermissionsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal actual val nativeDataModule = module {
    single<DataStoreProvider> { AndroidDataStoreProvider(androidContext()) }
    factory<LocaleProvider> { AndroidLocaleProvider(get()) }
    factoryOf(::PermissionsRepository)
}
