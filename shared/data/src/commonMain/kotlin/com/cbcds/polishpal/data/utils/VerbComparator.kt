package com.cbcds.polishpal.data.utils

import com.cbcds.polishpal.core.kotlin.polishCompareTo
import com.cbcds.polishpal.data.model.words.Verb
import kotlin.math.min

internal class VerbComparator : Comparator<Verb> {

    @Suppress("ReturnCount")
    override fun compare(v1: Verb, v2: Verb): Int {
        val inf1 = v1.infinitive
        val inf2 = v2.infinitive

        if (v1 == v2) return 0

        for (i in 0 until min(inf1.length, inf2.length)) {
            val diff = inf1[i].polishCompareTo(inf2[i])

            if (diff != 0) return diff
        }

        return inf1.length - inf2.length
    }
}
