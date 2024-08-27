package com.cbcds.polishpal.data.datasource.db.words

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.cbcds.polishpal.data.model.exercises.ExerciseType

@Entity(
    tableName = "exercise_results",
    foreignKeys = [
        ForeignKey(
            entity = VerbEntity::class,
            parentColumns = ["id"],
            childColumns = ["verb_id"],
        )
    ],
)
internal data class ExerciseResultsEntity(
    @ColumnInfo(name = "verb_id", index = true) val verbId: Int,
    @ColumnInfo(name = "type") val exerciseType: ExerciseType,
    @ColumnInfo(name = "result") val result: Float,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
