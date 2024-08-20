package com.cbcds.polishpal.data.datasource.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences.Key
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.Preferences as DataStorePreferences

internal class DataStorePreferences(
    private val dataStoreProvider: DataStoreProvider,
    ioDispatcher: CoroutineDispatcher,
) : Preferences {

    private val dataStore: DataStore<DataStorePreferences>
        get() = dataStoreProvider.dataStore

    private val coroutineScope = CoroutineScope(ioDispatcher + SupervisorJob())

    override fun readBoolean(key: String): Flow<Boolean?> {
        return readValue(booleanPreferencesKey(key))
    }

    override fun readInt(key: String): Flow<Int?> {
        return readValue(intPreferencesKey(key))
    }

    override fun readString(key: String): Flow<String?> {
        return readValue(stringPreferencesKey(key))
    }

    override fun readStringSet(key: String): Flow<Set<String>?> {
        return readValue(stringSetPreferencesKey(key))
    }

    override fun writeBoolean(key: String, value: Boolean) {
        return writeValue(booleanPreferencesKey(key), value)
    }

    override fun writeInt(key: String, value: Int) {
        writeValue(intPreferencesKey(key), value)
    }

    override fun writeString(key: String, value: String) {
        writeValue(stringPreferencesKey(key), value)
    }

    override fun writeStringSet(key: String, value: Set<String>) {
        writeValue(stringSetPreferencesKey(key), value)
    }

    private fun <T> readValue(key: Key<T>): Flow<T?> {
        return dataStore.data.map { prefs -> prefs[key] }
    }

    private fun <T> writeValue(key: Key<T>, value: T) {
        coroutineScope.launch {
            dataStore.edit { prefs ->
                prefs[key] = value
            }
        }
    }
}
