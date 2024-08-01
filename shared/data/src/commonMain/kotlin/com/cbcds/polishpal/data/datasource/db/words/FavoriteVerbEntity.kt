package com.cbcds.polishpal.data.datasource.db.words

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_verbs",
    foreignKeys = [
        ForeignKey(
            entity = VerbEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
        )
    ]
)
internal data class FavoriteVerbEntity(
    @PrimaryKey val id: Int,
)
