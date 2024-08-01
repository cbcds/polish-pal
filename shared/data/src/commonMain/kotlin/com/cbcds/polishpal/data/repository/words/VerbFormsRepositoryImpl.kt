package com.cbcds.polishpal.data.repository.words

import com.cbcds.polishpal.data.datasource.db.words.VerbEntity
import com.cbcds.polishpal.data.datasource.db.words.VerbsDao
import com.cbcds.polishpal.data.datasource.db.words.mappers.VerbMapper
import com.cbcds.polishpal.data.model.words.Verb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class VerbFormsRepositoryImpl(
    private val verbsDao: VerbsDao,
) : VerbFormsRepository {

    private val verbMapper = VerbMapper()

    override fun getVerb(id: Int): Flow<Verb?> {
        return verbsDao.getVerbAndIsFavorite(id).map { it.toVerb() }
    }

    private fun Map<VerbEntity, Boolean>.toVerb(): Verb? {
        val verbToFavorite = entries.firstOrNull()

        return verbToFavorite?.let {
            verbMapper.map(verb = verbToFavorite.key, favorite = verbToFavorite.value)
        }
    }
}
