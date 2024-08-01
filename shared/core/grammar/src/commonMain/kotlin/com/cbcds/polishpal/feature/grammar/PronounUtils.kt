package com.cbcds.polishpal.feature.grammar

import androidx.compose.runtime.Composable
import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.data.model.words.Gender
import com.cbcds.polishpal.data.model.words.Person
import com.cbcds.polishpal.shared.core.grammar.Res
import com.cbcds.polishpal.shared.core.grammar.label_gender_feminine
import com.cbcds.polishpal.shared.core.grammar.label_gender_masculine
import com.cbcds.polishpal.shared.core.grammar.label_gender_masculine_personal
import com.cbcds.polishpal.shared.core.grammar.label_gender_neuter
import com.cbcds.polishpal.shared.core.grammar.label_gender_non_masculine_personal
import org.jetbrains.compose.resources.stringResource

@Suppress("CyclomaticComplexMethod")
@Composable
fun Form.getPronoun(): String? {
    return when (person) {
        Person.FIRST -> {
            when (gender) {
                Gender.ALL_SINGULAR,
                Gender.MASCULINE,
                Gender.FEMININE -> "ja${gender.toPronounSuffix()}"
                Gender.ALL_PLURAL,
                Gender.MASCULINE_PERSONAL,
                Gender.NON_MASCULINE_PERSONAL -> "my${gender.toPronounSuffix()}"
                else -> null
            }
        }
        Person.SECOND -> {
            when (gender) {
                Gender.ALL_SINGULAR,
                Gender.MASCULINE,
                Gender.FEMININE -> "ty${gender.toPronounSuffix()}"
                Gender.ALL_PLURAL,
                Gender.MASCULINE_PERSONAL,
                Gender.NON_MASCULINE_PERSONAL -> "wy${gender.toPronounSuffix()}"
                else -> null
            }
        }
        Person.THIRD -> {
            when (gender) {
                Gender.ALL_SINGULAR -> "on\nona\nono"
                Gender.MASCULINE -> "on"
                Gender.FEMININE -> "ona"
                Gender.NEUTER -> "ono"
                Gender.ALL_PLURAL -> "oni\none"
                Gender.MASCULINE_PERSONAL -> "oni"
                Gender.NON_MASCULINE_PERSONAL -> "one"
            }
        }
    }
}

@Composable
fun Gender.toPronounSuffix(): String {
    val resId = when (this) {
        Gender.MASCULINE -> Res.string.label_gender_masculine
        Gender.FEMININE -> Res.string.label_gender_feminine
        Gender.NEUTER -> Res.string.label_gender_neuter
        Gender.MASCULINE_PERSONAL -> Res.string.label_gender_masculine_personal
        Gender.NON_MASCULINE_PERSONAL -> Res.string.label_gender_non_masculine_personal
        else -> null
    }

    return resId?.let { "\n(${stringResource(it)})" }.orEmpty()
}
