package com.cbcds.polishpal.data.di

import com.cbcds.polishpal.core.kotlin.di.IO_DISPATCHER
import com.cbcds.polishpal.data.datasource.db.AppDatabase
import com.cbcds.polishpal.data.datasource.db.DatabaseProvider
import com.cbcds.polishpal.data.datasource.db.words.VerbsDao
import com.cbcds.polishpal.data.datasource.prefs.DataStorePreferences
import com.cbcds.polishpal.data.datasource.prefs.Preferences
import com.cbcds.polishpal.data.repository.exercises.ExercisesSettingsRepository
import com.cbcds.polishpal.data.repository.exercises.ExercisesSettingsRepositoryImpl
import com.cbcds.polishpal.data.repository.settings.AppSettingsRepository
import com.cbcds.polishpal.data.repository.settings.AppSettingsRepositoryImpl
import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepository
import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepositoryImpl
import com.cbcds.polishpal.data.repository.words.VerbFormsRepository
import com.cbcds.polishpal.data.repository.words.VerbFormsRepositoryImpl
import com.cbcds.polishpal.data.repository.words.VocabularyRepository
import com.cbcds.polishpal.data.repository.words.VocabularyRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module

val dataModule = module {
    includes(nativeDataModule)

    factory<Preferences> {
        DataStorePreferences(
            dataStoreProvider = get(),
            ioDispatcher = get(named(IO_DISPATCHER)),
        )
    }

    single<AppSettingsRepository>(createdAtStart = true) {
        AppSettingsRepositoryImpl(
            preferences = get(),
            localeProvider = get(),
            externalScope = get(),
            ioDispatcher = get(named(IO_DISPATCHER)),
        )
    }

    single<AppDatabase> { get<DatabaseProvider>().getDatabase() }

    factory<VerbsDao> { get<AppDatabase>().getVerbsDao() }
    factoryOf(::FavoriteVerbsRepositoryImpl) binds arrayOf(FavoriteVerbsRepository::class)
    factoryOf(::VerbFormsRepositoryImpl) binds arrayOf(VerbFormsRepository::class)
    factoryOf(::VocabularyRepositoryImpl) binds arrayOf(VocabularyRepository::class)

    factoryOf(::ExercisesSettingsRepositoryImpl) binds arrayOf(ExercisesSettingsRepository::class)
}

internal expect val nativeDataModule: Module
