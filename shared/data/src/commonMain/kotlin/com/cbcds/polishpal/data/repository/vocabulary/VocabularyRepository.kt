package com.cbcds.polishpal.data.repository.vocabulary

import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepository
import com.cbcds.polishpal.data.repository.words.VerbFormsRepository
import kotlinx.coroutines.flow.Flow

interface VocabularyRepository : FavoriteVerbsRepository, VerbFormsRepository {

    fun getAllVerbInfinitives(
        prefix: String,
        onlyFavorite: Boolean,
        aspect: Aspect?,
    ): Flow<List<Verb>>
}
