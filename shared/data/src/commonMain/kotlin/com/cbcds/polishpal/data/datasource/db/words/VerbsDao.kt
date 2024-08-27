package com.cbcds.polishpal.data.datasource.db.words

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.Query
import com.cbcds.polishpal.data.datasource.db.converters.ExerciseTypeConverter.Companion.CONDITIONAL_MOOD
import com.cbcds.polishpal.data.datasource.db.converters.ExerciseTypeConverter.Companion.IMPERATIVE_MOOD
import com.cbcds.polishpal.data.datasource.db.converters.ExerciseTypeConverter.Companion.INDICATIVE_MOOD_FUTURE_TENSE
import com.cbcds.polishpal.data.datasource.db.converters.ExerciseTypeConverter.Companion.INDICATIVE_MOOD_PAST_TENSE
import com.cbcds.polishpal.data.datasource.db.converters.ExerciseTypeConverter.Companion.INDICATIVE_MOOD_PRESENT_TENSE
import com.cbcds.polishpal.data.model.words.Aspect
import kotlinx.coroutines.flow.Flow

@Dao
internal interface VerbsDao {

    @Query(
        "SELECT DISTINCT v.id, fv.id IS NOT NULL AS fav, * " +
            "FROM (SELECT * FROM verbs WHERE id = :id LIMIT 1) v " +
            "LEFT JOIN favorite_verbs fv ON v.id = fv.id"
    )
    fun getVerbAndIsFavorite(
        id: Int,
    ): Flow<Map<VerbEntity, @MapColumn(columnName = "fav") Boolean>>

    @Query(
        "SELECT DISTINCT v.id, fv.id IS NOT NULL AS fav, * " +
        "FROM (" +
            "SELECT ROUND(v.priority * (1 - IFNULL(result, 0)), 2) * 10000 + ABS(RANDOM() % 100) as comp_priority, * " +
            "FROM (SELECT * FROM verbs WHERE dkn IN (:aspects)) v " +
            "LEFT JOIN (" +
                "SELECT SUM(result) / 3 as result, * " +
                "FROM (" +
                    "SELECT verb_id, MAX(result) as result FROM exercise_results " +
                    "WHERE type IN ('$INDICATIVE_MOOD_PAST_TENSE', '$INDICATIVE_MOOD_PRESENT_TENSE', '$INDICATIVE_MOOD_FUTURE_TENSE') " +
                    "GROUP BY verb_id, type" +
                ") " +
                "GROUP BY verb_id" +
            ") res ON v.id = res.verb_id " +
            "ORDER BY comp_priority DESC " +
            "LIMIT :wordsNumber" +
        ") v " +
        "LEFT JOIN favorite_verbs fv ON v.id = fv.id"
    )
    fun getVerbIndicativeMoodAndIsFavorite(
        aspects: Set<Aspect>,
        wordsNumber: Int,
    ): Flow<Map<VerbEntity, @MapColumn(columnName = "fav") Boolean>>

    @Query(
        "SELECT DISTINCT v.id, fv.id IS NOT NULL AS fav, * " +
        "FROM (" +
            "SELECT ROUND(v.priority * (1 - IFNULL(result, 0)), 2) * 10000 + ABS(RANDOM() % 100) as comp_priority, * " +
            "FROM verbs v " +
            "LEFT JOIN (" +
                "SELECT verb_id, MAX(result) as result FROM exercise_results " +
                "WHERE type = '$IMPERATIVE_MOOD' " +
                "GROUP BY verb_id" +
            ") res ON v.id = res.verb_id " +
            "ORDER BY comp_priority DESC " +
            "LIMIT :wordsNumber" +
        ") v " +
        "LEFT JOIN favorite_verbs fv ON v.id = fv.id"
    )
    fun getVerbImperativeMoodAndIsFavorite(
        wordsNumber: Int,
    ): Flow<Map<VerbEntity, @MapColumn(columnName = "fav") Boolean>>

    @Query(
        "SELECT DISTINCT v.id, fv.id IS NOT NULL AS fav, * " +
        "FROM (" +
            "SELECT ROUND(v.priority * (1 - IFNULL(result, 0)), 2) * 10000 + ABS(RANDOM() % 100) as comp_priority, * " +
            "FROM (SELECT * FROM verbs WHERE rozk_lm_1os IS NOT NULL) v " +
            "LEFT JOIN (" +
                "SELECT verb_id, MAX(result) as result FROM exercise_results " +
                "WHERE type = '$CONDITIONAL_MOOD' " +
                "GROUP BY verb_id" +
            ") res ON v.id = res.verb_id " +
            "ORDER BY comp_priority DESC " +
            "LIMIT :wordsNumber" +
        ") v " +
        "LEFT JOIN favorite_verbs fv ON v.id = fv.id"
    )
    fun getVerbConditionalMoodAndIsFavorite(
        wordsNumber: Int,
    ): Flow<Map<VerbEntity, @MapColumn(columnName = "fav") Boolean>>

    @Query(
        "SELECT DISTINCT v.id, fv.id IS NOT NULL AS fav, v.inf, v.dkn " +
            "FROM verbs v " +
            "LEFT JOIN favorite_verbs fv ON v.id = fv.id"
    )
    fun getAllVerbInfinitives(): Flow<List<InfinitiveVerb>>

    @Query(
        "SELECT DISTINCT v.id, fv.id IS NOT NULL AS fav, v.inf, v.dkn " +
            "FROM (SELECT * FROM verbs WHERE id IN (:ids)) v " +
            "LEFT JOIN favorite_verbs fv ON v.id = fv.id"
    )
    fun getVerbInfinitives(ids: Set<Int>): Flow<List<InfinitiveVerb>>

    @Query(
        "SELECT DISTINCT v.id, fv.id IS NOT NULL AS fav, v.inf, v.dkn, v.def " +
            "FROM (SELECT * FROM verbs WHERE id = :id LIMIT 1) v " +
            "LEFT JOIN favorite_verbs fv ON v.id = fv.id"
    )
    fun getVerbInfinitiveWithDefinition(id: Int): Flow<InfinitiveVerbWithDefinition>

    @Query("INSERT OR IGNORE INTO favorite_verbs (id) VALUES (:id)")
    suspend fun markVerbAsFavorite(id: Int)

    @Query("DELETE FROM favorite_verbs WHERE id = :id")
    suspend fun unmarkVerbAsFavorite(id: Int)

    @Insert
    suspend fun saveExerciseResult(result: ExerciseResultsEntity)
}
