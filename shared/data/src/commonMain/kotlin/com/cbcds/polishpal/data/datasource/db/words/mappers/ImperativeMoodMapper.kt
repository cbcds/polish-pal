package com.cbcds.polishpal.data.datasource.db.words.mappers

import com.cbcds.polishpal.data.datasource.db.words.VerbEntity
import com.cbcds.polishpal.data.datasource.db.words.mappers.VerbMapper.Companion.toForm
import com.cbcds.polishpal.data.model.words.Gender
import com.cbcds.polishpal.data.model.words.MoodForms
import com.cbcds.polishpal.data.model.words.Person

internal class ImperativeMoodMapper {

    fun getImperativeMood(verb: VerbEntity): MoodForms.Imperative? {
        return if (verb.rozk_lp_2os != null ||
            verb.rozk_lm_1os != null ||
            verb.rozk_lm_2os != null
        ) {
            val forms = listOfNotNull(
                verb.rozk_lp_2os?.toForm(Person.SECOND, Gender.ALL_SINGULAR),
                verb.rozk_lm_1os?.toForm(Person.FIRST, Gender.ALL_PLURAL),
                verb.rozk_lm_2os?.toForm(Person.SECOND, Gender.ALL_PLURAL),
            )

            MoodForms.Imperative(forms)
        } else {
            null
        }
    }
}
