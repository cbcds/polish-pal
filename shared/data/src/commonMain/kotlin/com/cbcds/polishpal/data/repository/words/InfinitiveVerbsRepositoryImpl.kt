package com.cbcds.polishpal.data.repository.words

import com.cbcds.polishpal.data.datasource.db.words.InfinitiveVerb
import com.cbcds.polishpal.data.datasource.db.words.InfinitiveVerbWithDefinition
import com.cbcds.polishpal.data.datasource.db.words.VerbsDao
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.data.utils.VerbComparator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class InfinitiveVerbsRepositoryImpl(
    private val verbsDao: VerbsDao,
    private val defaultDispatcher: CoroutineDispatcher,
) : InfinitiveVerbsRepository {

    private val comparator = VerbComparator()

    override fun getAllVerbInfinitives(sorted: Boolean): Flow<List<Verb>> {
        return verbsDao.getAllVerbInfinitives()
            .map { dbVerbs ->
                dbVerbs
                    .map { it.toVerb() }
                    .let { if (sorted) it.sortedWith(comparator) else it }
            }
            .flowOn(defaultDispatcher)
    }

    override fun getVerbInfinitives(ids: Set<Int>, sorted: Boolean): Flow<List<Verb>> {
        return verbsDao.getVerbInfinitives(ids)
            .map { dbVerbs ->
                dbVerbs
                    .map { it.toVerb() }
                    .let { if (sorted) it.sortedWith(comparator) else it }
            }
            .flowOn(defaultDispatcher)
    }

    override fun getVerbInfinitiveWithDefinition(id: Int): Flow<Verb> {
        return verbsDao.getVerbInfinitiveWithDefinition(id)
            .map { it.toVerb() }
            .flowOn(defaultDispatcher)
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
