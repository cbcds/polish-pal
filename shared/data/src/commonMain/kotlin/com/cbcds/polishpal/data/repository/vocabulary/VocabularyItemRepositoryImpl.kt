package com.cbcds.polishpal.data.repository.vocabulary

import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepository
import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepositoryImpl
import com.cbcds.polishpal.data.repository.words.VerbFormsRepository
import com.cbcds.polishpal.data.repository.words.VerbFormsRepositoryImpl

internal class VocabularyItemRepositoryImpl(
    private val favoriteVerbsRepository: FavoriteVerbsRepositoryImpl,
    private val verbFormsRepository: VerbFormsRepositoryImpl,
) : VocabularyItemRepository,
    FavoriteVerbsRepository by favoriteVerbsRepository,
    VerbFormsRepository by verbFormsRepository
