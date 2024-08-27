package com.cbcds.polishpal.data.repository.vocabulary

import com.cbcds.polishpal.core.kotlin.withoutDiacritics
import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepository
import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepositoryImpl
import com.cbcds.polishpal.data.repository.words.InfinitiveVerbsRepositoryImpl
import com.cbcds.polishpal.data.repository.words.VerbFormsRepository
import com.cbcds.polishpal.data.repository.words.VerbFormsRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class VocabularyRepositoryImpl(
    private val infinitiveVerbsRepository: InfinitiveVerbsRepositoryImpl,
    private val favoriteVerbsRepository: FavoriteVerbsRepositoryImpl,
    private val verbFormsRepository: VerbFormsRepositoryImpl,
) : VocabularyRepository,
    FavoriteVerbsRepository by favoriteVerbsRepository,
    VerbFormsRepository by verbFormsRepository {

    private val infinitiveVerbs: Flow<List<Verb>> by lazy {
        infinitiveVerbsRepository.getAllVerbInfinitives(sorted = true)
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

    private fun String.startsWithIgnoreCaseAndDiacritics(prefix: String): Boolean {
        return prefix.isEmpty() ||
            startsWith(prefix, ignoreCase = true) ||
            withoutDiacritics().startsWith(prefix, ignoreCase = true)
    }
}
