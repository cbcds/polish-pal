package com.cbcds.polishpal.data.repository.exercises

import com.cbcds.polishpal.core.kotlin.runSuspendCatching
import com.cbcds.polishpal.data.datasource.db.words.ExerciseResultsEntity
import com.cbcds.polishpal.data.datasource.db.words.InfinitiveVerb
import com.cbcds.polishpal.data.datasource.db.words.InfinitiveVerbWithDefinition
import com.cbcds.polishpal.data.datasource.db.words.VerbEntity
import com.cbcds.polishpal.data.datasource.db.words.VerbsDao
import com.cbcds.polishpal.data.datasource.db.words.mappers.VerbMapper
import com.cbcds.polishpal.data.model.exercises.Exercise
import com.cbcds.polishpal.data.model.exercises.ExerciseSettings
import com.cbcds.polishpal.data.model.utils.toExerciseType
import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.data.model.words.Tense
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepository
import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

internal class ExerciseRepositoryImpl(
    private val verbsDao: VerbsDao,
    private val favoriteVerbsRepository: FavoriteVerbsRepositoryImpl,
    private val defaultDispatcher: CoroutineDispatcher,
) : ExerciseRepository,
    FavoriteVerbsRepository by favoriteVerbsRepository {

    private val verbMapper = VerbMapper()

    override suspend fun getVerbs(settings: ExerciseSettings): List<Verb> {
        val verbs = runSuspendCatching { getVerbsUnsafe(settings) }
            .getOrNull()
            ?.firstOrNull()
            ?.let {
                withContext(defaultDispatcher) { it.toVerbs() }
            }
            .orEmpty()

        return verbs
    }

    override fun getVerbInfinitiveWithDefinition(id: Int): Flow<Verb> {
        val verb = runCatching { verbsDao.getVerbInfinitiveWithDefinition(id) }
            .getOrNull()
            ?.map { it.toVerb() }
            ?.flowOn(defaultDispatcher)
            ?: emptyFlow()

        return verb
    }

    override fun getVerbInfinitives(ids: Set<Int>): Flow<List<Verb>> {
        val verbs = runCatching { verbsDao.getVerbInfinitives(ids) }
            .getOrNull()
            ?.map { dbVerbs ->
                dbVerbs.map { it.toVerb() }
            }
            ?.flowOn(defaultDispatcher)
            ?: emptyFlow()

        return verbs
    }

    private fun getVerbsUnsafe(settings: ExerciseSettings): Flow<Map<VerbEntity, Boolean>> {
        return when (settings) {
            is ExerciseSettings.IndicativeMood -> {
                val aspects = if (
                    Aspect.PERFECTIVE in settings.aspects &&
                    settings.tenses.size == 1 &&
                    settings.tenses.first() == Tense.PRESENT
                ) {
                    settings.aspects - Aspect.PERFECTIVE
                } else {
                    settings.aspects
                }
                verbsDao.getVerbIndicativeMoodAndIsFavorite(aspects, settings.wordsNumber)
            }
            is ExerciseSettings.ImperativeMood ->
                verbsDao.getVerbImperativeMoodAndIsFavorite(settings.wordsNumber)
            is ExerciseSettings.ConditionalMood ->
                verbsDao.getVerbConditionalMoodAndIsFavorite(settings.wordsNumber)
        }
    }

    override suspend fun saveResult(exercise: Exercise, result: Float) {
        if (result > 0f) {
            runSuspendCatching {
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
    }

    private fun Map<VerbEntity, Boolean>.toVerbs(): List<Verb> {
        return entries.map { (verb, favorite) ->
            verbMapper.map(verb = verb, favorite = favorite)
        }
    }

    private fun InfinitiveVerb.toVerb(): Verb {
        return Verb(
            id = id,
            infinitive = name,
            aspect = aspect,
            favorite = favorite,
        )
    }

    private fun InfinitiveVerbWithDefinition.toVerb(): Verb {
        return Verb(
            id = id,
            infinitive = name,
            aspect = aspect,
            favorite = favorite,
            definition = definition,
        )
    }
}
