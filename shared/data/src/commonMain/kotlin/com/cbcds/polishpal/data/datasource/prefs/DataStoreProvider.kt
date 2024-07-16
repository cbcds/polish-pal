package com.cbcds.polishpal.data.datasource.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

internal abstract class DataStoreProvider(
    private val getFilePath: (filename: String) -> String
) {

    val dataStore: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { getFilePath(DATASTORE_FILENAME).toPath() }
        )
    }

    private companion object {

        const val DATASTORE_FILENAME = "app.preferences_pb"
    }
}
