package com.cbcds.polishpal.data.di

import com.cbcds.polishpal.core.kotlin.di.IO_DISPATCHER
import com.cbcds.polishpal.data.datasource.AndroidLocaleProvider
import com.cbcds.polishpal.data.datasource.LocaleProvider
import com.cbcds.polishpal.data.datasource.db.AndroidDatabaseProvider
import com.cbcds.polishpal.data.datasource.db.DatabaseProvider
import com.cbcds.polishpal.data.datasource.prefs.AndroidDataStoreProvider
import com.cbcds.polishpal.data.datasource.prefs.DataStoreProvider
import com.cbcds.polishpal.data.repository.settings.PermissionsRepository
import com.cbcds.polishpal.data.repository.settings.PermissionsRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val nativeDataModule = module {
    single<DataStoreProvider> { AndroidDataStoreProvider(androidContext()) }

    single<DatabaseProvider> {
        AndroidDatabaseProvider(
            context = androidContext(),
            ioDispatcher = get(named(IO_DISPATCHER)),
        )
    }

    factory<LocaleProvider> { AndroidLocaleProvider(get()) }

    factory<PermissionsRepository> { PermissionsRepositoryImpl(preferences = get()) }
}
