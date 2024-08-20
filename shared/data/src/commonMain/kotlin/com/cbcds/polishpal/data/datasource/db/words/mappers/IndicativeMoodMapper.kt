package com.cbcds.polishpal.data.datasource.db.words.mappers

import com.cbcds.polishpal.data.datasource.db.words.VerbEntity
import com.cbcds.polishpal.data.datasource.db.words.mappers.VerbMapper.Companion.toForm
import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.data.model.words.Gender
import com.cbcds.polishpal.data.model.words.MoodForms
import com.cbcds.polishpal.data.model.words.Person
import com.cbcds.polishpal.data.model.words.TenseForms
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList

internal class IndicativeMoodMapper {

    fun getIndicativeMood(verb: VerbEntity): MoodForms.Indicative {
        val pastTense = verb.getPastTense()
        val presentTense = verb.getPresentTense()
        val futureTense = verb.getFutureTense(pastTense)

        return MoodForms.Indicative(
            pastTense = pastTense,
            presentTense = presentTense,
            futureTense = futureTense,
        )
    }

    private fun VerbEntity.getPastTense(): TenseForms.Past {
        val forms = persistentListOf(
            ozn_przesz_lp_1os_rm.toForm(Person.FIRST, Gender.MASCULINE),
            ozn_przesz_lp_1os_rz.toForm(Person.FIRST, Gender.FEMININE),
            ozn_przesz_lp_2os_rm.toForm(Person.SECOND, Gender.MASCULINE),
            ozn_przesz_lp_2os_rz.toForm(Person.SECOND, Gender.FEMININE),
            ozn_przesz_lp_3os_rm.toForm(Person.THIRD, Gender.MASCULINE),
            ozn_przesz_lp_3os_rz.toForm(Person.THIRD, Gender.FEMININE),
            ozn_przesz_lp_3os_rn.toForm(Person.THIRD, Gender.NEUTER),
            ozn_przesz_lm_1os_rmo.toForm(Person.FIRST, Gender.MASCULINE_PERSONAL),
            ozn_przesz_lm_1os_rnmo.toForm(Person.FIRST, Gender.NON_MASCULINE_PERSONAL),
            ozn_przesz_lm_2os_rmo.toForm(Person.SECOND, Gender.MASCULINE_PERSONAL),
            ozn_przesz_lm_2os_rnmo.toForm(Person.SECOND, Gender.NON_MASCULINE_PERSONAL),
            ozn_przesz_lm_3os_rmo.toForm(Person.THIRD, Gender.MASCULINE_PERSONAL),
            ozn_przesz_lm_3os_rnmo.toForm(Person.THIRD, Gender.NON_MASCULINE_PERSONAL),
        )

        return TenseForms.Past(forms)
    }

    private fun VerbEntity.getPresentTense(): TenseForms.Present? {
        return when (aspect) {
            Aspect.IMPERFECTIVE -> TenseForms.Present(getPresentOrFutureFormsForPerfectiveAspect())
            Aspect.PERFECTIVE -> null
        }
    }

    private fun VerbEntity.getFutureTense(pastTense: TenseForms.Past?): TenseForms.Future {
        return when (aspect) {
            Aspect.IMPERFECTIVE -> TenseForms.Future(getFutureFormsForImperfectiveAspect(pastTense ?: getPastTense()))
            Aspect.PERFECTIVE -> TenseForms.Future(getPresentOrFutureFormsForPerfectiveAspect())
        }
    }

    private fun VerbEntity.getPresentOrFutureFormsForPerfectiveAspect(): ImmutableList<Form> {
        return persistentListOf(
            ozn_trz_przy_lp_1os.toForm(Person.FIRST, Gender.ALL_SINGULAR),
            ozn_trz_przy_lp_2os.toForm(Person.SECOND, Gender.ALL_SINGULAR),
            ozn_trz_przy_lp_3os.toForm(Person.THIRD, Gender.ALL_SINGULAR),
            ozn_trz_przy_lm_1os.toForm(Person.FIRST, Gender.ALL_PLURAL),
            ozn_trz_przy_lm_2os.toForm(Person.SECOND, Gender.ALL_PLURAL),
            ozn_trz_przy_lm_3os.toForm(Person.THIRD, Gender.ALL_PLURAL),
        )
    }

    private fun VerbEntity.getFutureFormsForImperfectiveAspect(
        pastTense: TenseForms.Past
    ): ImmutableList<Form> {
        val pastTenseForms = pastTense.forms
        val forms = pastTenseForms.mapNotNull { form ->
            pastToFutureTense(infinitive, pastTenseForms, form.gender, form.person)
        }.toImmutableList()

        return forms
    }

    private fun pastToFutureTense(
        infinitive: String,
        pastTenseForms: List<Form>,
        gender: Gender,
        person: Person,
    ): Form? {
        val beForm = BE_FUTURE_TENSE_FORMS
            .first { it.person == person && it.gender.number == gender.number }
            .values
            .first()

        val verbForms = pastTenseForms
            .find { it.person == Person.THIRD && it.gender == gender }
            ?.values
            ?: return null

        val forms = verbForms.map { "$beForm $it" }.toPersistentList() +
            "$beForm $infinitive"

        return Form(
            values = forms,
            person = person,
            gender = gender,
        )
    }

    private companion object {

        val BE_FUTURE_TENSE_FORMS = listOf(
            "będę".toForm(Person.FIRST, Gender.ALL_SINGULAR),
            "będziesz".toForm(Person.SECOND, Gender.ALL_SINGULAR),
            "będzie".toForm(Person.THIRD, Gender.ALL_SINGULAR),
            "będziemy".toForm(Person.FIRST, Gender.ALL_PLURAL),
            "będziecie".toForm(Person.SECOND, Gender.ALL_PLURAL),
            "będą".toForm(Person.THIRD, Gender.ALL_PLURAL),
        )
    }
}
