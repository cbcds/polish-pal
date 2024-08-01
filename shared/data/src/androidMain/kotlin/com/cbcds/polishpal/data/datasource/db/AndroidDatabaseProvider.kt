package com.cbcds.polishpal.data.datasource.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineDispatcher

internal class AndroidDatabaseProvider(
    private val context: Context,
    ioDispatcher: CoroutineDispatcher,
) : DatabaseProvider(ioDispatcher) {

    override fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
        return Room
            .databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_FILENAME)
            .createFromAsset(ASSETS_DB_FILEPATH)
    }

    private companion object {

        const val DB_FILENAME = "app.db"
        const val ASSETS_DB_FILEPATH = "db/verbs.db"
    }
}
