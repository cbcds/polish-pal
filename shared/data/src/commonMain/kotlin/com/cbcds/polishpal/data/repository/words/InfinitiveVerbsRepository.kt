package com.cbcds.polishpal.data.repository.words

import com.cbcds.polishpal.data.model.words.Verb
import kotlinx.coroutines.flow.Flow

interface InfinitiveVerbsRepository {

    fun getAllVerbInfinitives(sorted: Boolean): Flow<List<Verb>>

    fun getVerbInfinitives(ids: Set<Int>, sorted: Boolean): Flow<List<Verb>>

    fun getVerbInfinitiveWithDefinition(id: Int): Flow<Verb>
}
