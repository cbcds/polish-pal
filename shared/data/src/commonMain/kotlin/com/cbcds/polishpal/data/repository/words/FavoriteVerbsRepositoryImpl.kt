package com.cbcds.polishpal.data.repository.words

import com.cbcds.polishpal.data.datasource.db.words.VerbsDao

internal class FavoriteVerbsRepositoryImpl(
    private val verbsDao: VerbsDao,
) : FavoriteVerbsRepository {

    override suspend fun setIsVerbFavorite(id: Int, favorite: Boolean) {
        if (favorite) {
            verbsDao.markVerbAsFavorite(id)
        } else {
            verbsDao.unmarkVerbAsFavorite(id)
        }
    }
}
