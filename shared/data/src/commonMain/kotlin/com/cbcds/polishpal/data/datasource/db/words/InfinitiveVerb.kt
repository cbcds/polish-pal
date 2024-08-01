package com.cbcds.polishpal.data.datasource.db.words

import androidx.room.ColumnInfo
import com.cbcds.polishpal.data.model.words.Aspect

internal data class InfinitiveVerb(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "inf") val name: String,
    @ColumnInfo(name = "dkn") val aspect: Aspect,
    @ColumnInfo(name = "fav") val favorite: Boolean,
)
