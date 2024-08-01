package com.cbcds.polishpal.data.datasource.db.words

import androidx.room.Dao
import androidx.room.MapColumn
import androidx.room.Query
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
    ): Flow<
        Map<
            VerbEntity,
            @MapColumn(columnName = "fav")
            Boolean
            >
        >

    @Query(
        "SELECT DISTINCT v.id, fv.id IS NOT NULL AS fav, v.inf, v.dkn " +
            "FROM verbs v " +
            "LEFT JOIN favorite_verbs fv ON v.id = fv.id " +
            "ORDER BY v.inf"
    )
    fun getAllVerbInfinitives(): Flow<List<InfinitiveVerb>>

    @Query("INSERT OR IGNORE INTO favorite_verbs (id) VALUES (:id)")
    suspend fun markVerbAsFavorite(id: Int)

    @Query("DELETE FROM favorite_verbs WHERE id = :id")
    suspend fun unmarkVerbAsFavorite(id: Int)
}
