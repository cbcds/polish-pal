package com.cbcds.polishpal.data.repository.vocabulary

import com.cbcds.polishpal.data.repository.words.FavoriteVerbsRepository
import com.cbcds.polishpal.data.repository.words.VerbFormsRepository

interface VocabularyItemRepository : FavoriteVerbsRepository, VerbFormsRepository
