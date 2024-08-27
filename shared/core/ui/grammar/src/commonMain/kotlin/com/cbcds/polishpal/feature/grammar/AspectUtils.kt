package com.cbcds.polishpal.feature.grammar

import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.shared.core.grammar.Res
import com.cbcds.polishpal.shared.core.grammar.aspect_name_imperfective
import com.cbcds.polishpal.shared.core.grammar.aspect_name_perfective
import org.jetbrains.compose.resources.StringResource

fun Aspect.toStringResource(): StringResource {
    return when (this) {
        Aspect.IMPERFECTIVE -> Res.string.aspect_name_imperfective
        Aspect.PERFECTIVE -> Res.string.aspect_name_perfective
    }
}
