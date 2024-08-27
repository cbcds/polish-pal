package com.cbcds.polishpal.feature.grammar

import com.cbcds.polishpal.data.model.words.Tense
import com.cbcds.polishpal.shared.core.grammar.Res
import com.cbcds.polishpal.shared.core.grammar.tense_name_future
import com.cbcds.polishpal.shared.core.grammar.tense_name_past
import com.cbcds.polishpal.shared.core.grammar.tense_name_present
import com.cbcds.polishpal.shared.core.grammar.title_tense_future
import com.cbcds.polishpal.shared.core.grammar.title_tense_past
import com.cbcds.polishpal.shared.core.grammar.title_tense_present
import org.jetbrains.compose.resources.StringResource

fun Tense.toStringResource(): StringResource {
    return when (this) {
        Tense.PAST -> Res.string.tense_name_past
        Tense.PRESENT -> Res.string.tense_name_present
        Tense.FUTURE -> Res.string.tense_name_future
    }
}

fun Tense.toTitleStringResource(): StringResource {
    return when (this) {
        Tense.PAST -> Res.string.title_tense_past
        Tense.PRESENT -> Res.string.title_tense_present
        Tense.FUTURE -> Res.string.title_tense_future
    }
}
