package com.cbcds.polishpal.data.repository.words

import com.cbcds.polishpal.core.kotlin.withoutDiacritics
import com.cbcds.polishpal.data.datasource.db.words.VerbsDao
import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.data.model.words.Verb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class VocabularyRepositoryImpl(
    private val verbsDao: VerbsDao,
    private val favoriteVerbsRepository: FavoriteVerbsRepositoryImpl,
    private val verbFormsRepository: VerbFormsRepositoryImpl,
) : VocabularyRepository,
    FavoriteVerbsRepository by favoriteVerbsRepository,
    VerbFormsRepository by verbFormsRepository {

    private val infinitiveVerbs: Flow<List<Verb>> by lazy { getAllVerbInfinitives() }

    override suspend fun getAllVerbInfinitives(
        prefix: String,
        onlyFavorite: Boolean,
        aspect: Aspect?,
    ): Flow<List<Verb>> {
        return infinitiveVerbs.map { verbs ->
            verbs.filter {
                (!onlyFavorite || it.favorite) &&
                    (aspect == null || it.aspect == aspect) &&
                    it.infinitive.startsWithIgnoreCaseAndDiacritics(prefix)
            }
        }
    }

    private fun getAllVerbInfinitives(): Flow<List<Verb>> {
        return verbsDao.getAllVerbInfinitives().map { dbVerbs ->
            dbVerbs.map { verb ->
                Verb(
                    id = verb.id,
                    infinitive = verb.name,
                    aspect = verb.aspect,
                    favorite = verb.favorite,
                )
            }
        }
    }

    private fun String.startsWithIgnoreCaseAndDiacritics(prefix: String): Boolean {
        return prefix.isEmpty() ||
            startsWith(prefix, ignoreCase = true) ||
            withoutDiacritics().startsWith(prefix, ignoreCase = true)
    }
}
