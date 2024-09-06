package com.cbcds.polishpal.data.repository.vocabulary

import com.cbcds.polishpal.core.kotlin.withoutDiacritics
import com.cbcds.polishpal.data.datasource.db.words.InfinitiveVerb
import com.cbcds.polishpal.data.datasource.db.words.VerbsDao
import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepository
import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepositoryImpl
import com.cbcds.polishpal.data.repository.words.VerbFormsRepository
import com.cbcds.polishpal.data.repository.words.VerbFormsRepositoryImpl
import com.cbcds.polishpal.data.utils.VerbComparator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class VocabularyRepositoryImpl(
    private val verbsDao: VerbsDao,
    private val favoriteVerbsRepository: FavoriteVerbsRepositoryImpl,
    private val verbFormsRepository: VerbFormsRepositoryImpl,
    private val defaultDispatcher: CoroutineDispatcher,
) : VocabularyRepository,
    FavoriteVerbsRepository by favoriteVerbsRepository,
    VerbFormsRepository by verbFormsRepository {

    private val comparator = VerbComparator()

    private val infinitiveVerbs: Flow<List<Verb>> by lazy {
        getAllVerbInfinitives()
    }

    override fun getAllVerbInfinitives(
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
        val verbs = runCatching { verbsDao.getAllVerbInfinitives() }
            .getOrNull()
            ?.map { dbVerbs ->
                dbVerbs
                    .map { it.toVerb() }
                    .sortedWith(comparator)
            }
            ?.flowOn(defaultDispatcher)
            ?: emptyFlow()

        return verbs
    }

    private fun InfinitiveVerb.toVerb(): Verb {
        return Verb(
            id = id,
            infinitive = name,
            aspect = aspect,
            favorite = favorite,
        )
    }

    private fun String.startsWithIgnoreCaseAndDiacritics(prefix: String): Boolean {
        return prefix.isEmpty() ||
            startsWith(prefix, ignoreCase = true) ||
            withoutDiacritics().startsWith(prefix, ignoreCase = true)
    }
}
