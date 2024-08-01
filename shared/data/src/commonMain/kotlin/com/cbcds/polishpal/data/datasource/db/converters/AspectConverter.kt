package com.cbcds.polishpal.data.datasource.db.converters

import androidx.room.TypeConverter
import com.cbcds.polishpal.data.model.words.Aspect

internal class AspectConverter {

    @TypeConverter
    fun toAspect(perfective: Boolean): Aspect {
        return if (perfective) Aspect.PERFECTIVE else Aspect.IMPERFECTIVE
    }

    @TypeConverter
    fun toBoolean(aspect: Aspect): Boolean {
        return when (aspect) {
            Aspect.PERFECTIVE -> true
            Aspect.IMPERFECTIVE -> false
        }
    }
}
