package com.cbcds.polishpal.data.repository.exercises

import com.cbcds.polishpal.data.model.exercises.Exercise
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepository
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository : FavoriteVerbsRepository {

    suspend fun getVerbs(settings: ExerciseSettings): List<Verb>

    fun getVerbInfinitiveWithDefinition(id: Int): Flow<Verb>

    fun getVerbInfinitives(ids: Set<Int>): Flow<List<Verb>>

    suspend fun saveResult(exercise: Exercise, result: Float)
}
