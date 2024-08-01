package com.cbcds.polishpal.data.repository.words

import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.data.model.words.Verb
import kotlinx.coroutines.flow.Flow

interface VocabularyRepository : FavoriteVerbsRepository, VerbFormsRepository {

    suspend fun getAllVerbInfinitives(
        prefix: String,
        onlyFavorite: Boolean,
        aspect: Aspect?,
    ): Flow<List<Verb>>
}
