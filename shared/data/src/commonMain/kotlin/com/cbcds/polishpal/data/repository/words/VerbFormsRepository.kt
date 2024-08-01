package com.cbcds.polishpal.data.repository.words

import com.cbcds.polishpal.data.model.words.Verb
import kotlinx.coroutines.flow.Flow

interface VerbFormsRepository {

    fun getVerb(id: Int): Flow<Verb?>
}
