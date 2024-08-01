package com.cbcds.polishpal.data.datasource.db.words.mappers

import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.data.model.words.Gender
import com.cbcds.polishpal.data.model.words.Mood
import com.cbcds.polishpal.data.model.words.Number
import com.cbcds.polishpal.data.model.words.Person
import com.cbcds.polishpal.data.model.words.Tense

internal class ConditionalMoodMapper {

    fun getConditionalMood(pastTense: Tense.Past): Mood.Conditional {
        val pastTenseForms = pastTense.forms
        val forms = pastTenseForms.mapNotNull { form ->
            pastTenseToConditionalMood(pastTenseForms, form.gender, form.person)
        }

        return Mood.Conditional(forms)
    }

    private fun pastTenseToConditionalMood(
        pastTenseForms: List<Form>,
        gender: Gender,
        person: Person,
    ): Form? {
        val base = pastTenseForms
            .find { it.person == Person.THIRD && it.gender == gender }
            ?.values
            ?: return null

        val ending = when (gender.number) {
            Number.SINGULAR -> {
                when (person) {
                    Person.FIRST -> "bym"
                    Person.SECOND -> "byś"
                    Person.THIRD -> "by"
                }
            }
            Number.PLURAL -> {
                when (person) {
                    Person.FIRST -> "byśmy"
                    Person.SECOND -> "byście"
                    Person.THIRD -> "by"
                }
            }
        }

        return Form(
            values = base.map { "$it$ending" },
            person = person,
            gender = gender,
        )
    }
}
