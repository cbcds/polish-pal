package com.cbcds.polishpal.data.repository.words

import com.cbcds.polishpal.core.kotlin.runSuspendCatching
import com.cbcds.polishpal.data.datasource.db.words.VerbsDao

internal class FavoriteVerbsRepositoryImpl(
    private val verbsDao: VerbsDao,
) : FavoriteVerbsRepository {

    override suspend fun setIsVerbFavorite(id: Int, favorite: Boolean) {
        if (favorite) {
            runSuspendCatching { verbsDao.markVerbAsFavorite(id) }
        } else {
            runSuspendCatching { verbsDao.unmarkVerbAsFavorite(id) }
        }
    }
}
