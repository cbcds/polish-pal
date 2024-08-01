package com.cbcds.polishpal.data.datasource.db.words.mappers

import com.cbcds.polishpal.data.datasource.db.words.VerbEntity
import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.data.model.words.Gender
import com.cbcds.polishpal.data.model.words.Person
import com.cbcds.polishpal.data.model.words.Verb

internal class VerbMapper {

    private val indicativeMoodMapper = IndicativeMoodMapper()
    private val imperativeMoodMapper = ImperativeMoodMapper()
    private val conditionalMoodMapper = ConditionalMoodMapper()

    fun map(verb: VerbEntity, favorite: Boolean): Verb {
        val indicativeMood = indicativeMoodMapper.getIndicativeMood(verb)

        return Verb(
            id = verb.id,
            aspect = verb.aspect,
            favorite = favorite,
            definition = verb.definition,
            infinitive = verb.infinitive,
            indicativeMood = indicativeMood,
            imperativeMood = imperativeMoodMapper.getImperativeMood(verb),
            conditionalMood = indicativeMood.pastTense
                ?.let(conditionalMoodMapper::getConditionalMood),
        )
    }

    companion object {

        private const val FORMS_DELIMITER = '/'

        fun String.toForm(person: Person, gender: Gender): Form {
            return Form(
                values = split(FORMS_DELIMITER),
                person = person,
                gender = gender,
            )
        }
    }
}
