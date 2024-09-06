package com.cbcds.polishpal.feature.vocabulary.screen.word

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.data.model.words.MoodForms
import com.cbcds.polishpal.shared.core.grammar.title_mood_conditional
import com.cbcds.polishpal.shared.core.grammar.title_mood_imperative
import com.cbcds.polishpal.shared.core.grammar.title_mood_indicative
import com.cbcds.polishpal.shared.core.grammar.title_tense_future
import com.cbcds.polishpal.shared.core.grammar.title_tense_past
import com.cbcds.polishpal.shared.core.grammar.title_tense_present
import com.cbcds.polishpal.shared.feature.vocabulary.Res
import com.cbcds.polishpal.shared.feature.vocabulary.no_forms_in_mood_exist
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import com.cbcds.polishpal.shared.core.grammar.Res as gramRes

@Composable
internal fun ColumnScope.MoodPager(
    pagerState: PagerState,
    tabsData: List<MoodTabData>,
) {
    val minTableWidth = LocalConfiguration.current.screenWidthDp.dp - 8.dp

    HorizontalPager(
        state = pagerState,
        verticalAlignment = Alignment.Top,
        userScrollEnabled = false,
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState()),
    ) { index ->
        when (val mood = tabsData.getOrNull(index)?.mood) {
            is MoodForms.Indicative -> IndicativeMoodForms(mood, minTableWidth)
            is MoodForms.Imperative? -> ImperativeMoodForms(mood, minTableWidth)
            is MoodForms.Conditional -> ConditionalMoodForms(mood, minTableWidth)
            else -> Unit
        }
    }
}

@Composable
private fun IndicativeMoodForms(
    mood: MoodForms.Indicative?,
    minTableWidth: Dp,
) {
    var listItemNumber = 1

    @Composable
    fun TenseForms(
        titleRes: StringResource,
        forms: ImmutableList<Form>
    ) {
        TenseTitle(listItemNumber, titleRes)
        FormsTable(
            forms = forms,
            accentColor = AppTheme.extendedColorScheme.accent1.colorContainer,
            onAccentColor = AppTheme.extendedColorScheme.accent1.onColorContainer,
            minWidth = minTableWidth,
        )
        Spacer(Modifier.height(8.dp))
        ++listItemNumber
    }

    Column {
        MoodTitle(gramRes.string.title_mood_indicative)

        if (mood != null) {
            mood.presentTense?.forms?.let { forms ->
                TenseForms(gramRes.string.title_tense_present, forms.toImmutableList())
            }
            mood.pastTense?.forms?.let { forms ->
                TenseForms(gramRes.string.title_tense_past, forms.toImmutableList())
            }
            mood.futureTense?.forms?.let { forms ->
                TenseForms(gramRes.string.title_tense_future, forms.toImmutableList())
            }
        } else {
            NoFormsMessage()
        }
    }
}

@Composable
private fun ImperativeMoodForms(
    mood: MoodForms.Imperative?,
    minTableWidth: Dp,
) {
    Column {
        MoodTitle(gramRes.string.title_mood_imperative)

        if (mood != null) {
            FormsTable(
                forms = mood.forms.toImmutableList(),
                accentColor = AppTheme.extendedColorScheme.accent2.colorContainer,
                onAccentColor = AppTheme.extendedColorScheme.accent2.onColorContainer,
                minWidth = minTableWidth,
            )
        } else {
            NoFormsMessage()
        }
    }
}

@Composable
private fun ConditionalMoodForms(
    mood: MoodForms.Conditional?,
    minTableWidth: Dp,
) {
    Column {
        MoodTitle(gramRes.string.title_mood_conditional)

        if (mood != null) {
            FormsTable(
                forms = mood.forms.toImmutableList(),
                accentColor = AppTheme.extendedColorScheme.accent3.colorContainer,
                onAccentColor = AppTheme.extendedColorScheme.accent3.onColorContainer,
                minWidth = minTableWidth,
            )
        } else {
            NoFormsMessage()
        }
    }
}

@Composable
private fun MoodTitle(textRes: StringResource) {
    Text(
        text = stringResource(textRes),
        style = AppTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 16.dp),
    )
}

@Composable
private fun TenseTitle(
    itemNumber: Int,
    textRes: StringResource,
) {
    Text(
        text = "$itemNumber. ${stringResource(textRes)}",
        style = AppTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
            .padding(vertical = 8.dp),
    )
}

@Composable
private fun NoFormsMessage() {
    Text(
        text = stringResource(Res.string.no_forms_in_mood_exist),
        style = AppTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
    )
}
