package com.cbcds.polishpal.data.repository.words

interface FavoriteVerbsRepository {

    suspend fun setIsVerbFavorite(id: Int, favorite: Boolean)
}
