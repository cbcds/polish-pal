package com.cbcds.polishpal.data.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cbcds.polishpal.data.datasource.db.converters.AspectConverter
import com.cbcds.polishpal.data.datasource.db.converters.ExerciseTypeConverter
import com.cbcds.polishpal.data.datasource.db.words.ExerciseResultsEntity
import com.cbcds.polishpal.data.datasource.db.words.FavoriteVerbEntity
import com.cbcds.polishpal.data.datasource.db.words.VerbEntity
import com.cbcds.polishpal.data.datasource.db.words.VerbsDao

@Database(
    entities = [VerbEntity::class, FavoriteVerbEntity::class, ExerciseResultsEntity::class],
    version = 1,
)
@TypeConverters(AspectConverter::class, ExerciseTypeConverter::class)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun getVerbsDao(): VerbsDao
}
