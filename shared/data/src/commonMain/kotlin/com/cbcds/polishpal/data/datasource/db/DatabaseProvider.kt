package com.cbcds.polishpal.data.datasource.db

import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineDispatcher

internal abstract class DatabaseProvider(
    private val ioDispatcher: CoroutineDispatcher,
) {

    fun getDatabase(): AppDatabase {
        return getDatabaseBuilder()
            .setQueryCoroutineContext(ioDispatcher)
            .build()
    }

    protected abstract fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
}
