package com.cbcds.polishpal.feature.vocabulary.screen.word

import com.cbcds.polishpal.data.model.words.Mood
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.shared.feature.vocabulary.Res
import com.cbcds.polishpal.shared.feature.vocabulary.tab_mood_conditional
import com.cbcds.polishpal.shared.feature.vocabulary.tab_mood_imperative
import com.cbcds.polishpal.shared.feature.vocabulary.tab_mood_indicative
import org.jetbrains.compose.resources.StringResource

internal data class MoodTabData(
    val titleRes: StringResource,
    val mood: Mood?,
)

internal fun Verb.getMoodTabsData(): List<MoodTabData> {
    return listOf(
        MoodTabData(Res.string.tab_mood_indicative, indicativeMood),
        MoodTabData(Res.string.tab_mood_imperative, imperativeMood),
        MoodTabData(Res.string.tab_mood_conditional, conditionalMood),
    )
}
