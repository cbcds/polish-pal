package com.cbcds.polishpal.data.repository.exercises

import com.cbcds.polishpal.data.datasource.db.words.ExerciseResultsEntity
import com.cbcds.polishpal.data.datasource.db.words.VerbEntity
import com.cbcds.polishpal.data.datasource.db.words.VerbsDao
import com.cbcds.polishpal.data.datasource.db.words.mappers.VerbMapper
import com.cbcds.polishpal.data.model.exercises.Exercise
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.data.model.utils.toExerciseType
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepository
import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepositoryImpl
import com.cbcds.polishpal.data.repository.words.InfinitiveVerbsRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

internal class ExerciseRepositoryImpl(
    private val verbsDao: VerbsDao,
    private val favoriteVerbsRepository: FavoriteVerbsRepositoryImpl,
    private val infinitiveVerbsRepository: InfinitiveVerbsRepositoryImpl,
    private val defaultDispatcher: CoroutineDispatcher,
) : ExerciseRepository,
    FavoriteVerbsRepository by favoriteVerbsRepository {

    private val verbMapper = VerbMapper()

    override suspend fun getVerbs(settings: ExerciseSettings): List<Verb> {
        val verbs = when (settings) {
            is ExerciseSettings.IndicativeMood ->
                verbsDao.getVerbIndicativeMoodAndIsFavorite(settings.aspects, settings.wordsNumber)
            is ExerciseSettings.ImperativeMood ->
                verbsDao.getVerbImperativeMoodAndIsFavorite(settings.wordsNumber)
            is ExerciseSettings.ConditionalMood ->
                verbsDao.getVerbConditionalMoodAndIsFavorite(settings.wordsNumber)
        }.firstOrNull()

        return verbs
            ?.let {
                withContext(defaultDispatcher) { it.toVerbs() }
            }
            .orEmpty()
    }

    override fun getVerbInfinitiveWithDefinition(id: Int): Flow<Verb> {
        return infinitiveVerbsRepository.getVerbInfinitiveWithDefinition(id)
    }

    override fun getVerbInfinitives(ids: Set<Int>): Flow<List<Verb>> {
        return infinitiveVerbsRepository.getVerbInfinitives(ids, sorted = false)
    }

    override suspend fun saveResult(exercise: Exercise, result: Float) {
        if (result > 0f) {
            verbsDao.saveExerciseResult(
                ExerciseResultsEntity(
                    verbId = exercise.verbId,
                    exerciseType = exercise.toExerciseType(),
                    result = result,
                    timestamp = Clock.System.now().toEpochMilliseconds(),
                )
            )
        }
    }

    private fun Map<VerbEntity, Boolean>.toVerbs(): List<Verb> {
        return entries.map { (verb, favorite) ->
            verbMapper.map(verb = verb, favorite = favorite)
        }
    }
}
