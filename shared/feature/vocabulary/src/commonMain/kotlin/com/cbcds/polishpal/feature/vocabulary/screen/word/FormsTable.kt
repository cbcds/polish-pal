package com.cbcds.polishpal.feature.vocabulary.screen.word

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.times
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.core.ui.utils.measureTextWidth
import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.data.model.words.Number
import com.cbcds.polishpal.feature.grammar.getMaxPronounLabelWidth
import com.cbcds.polishpal.feature.grammar.getPronounLabel
import com.cbcds.polishpal.shared.core.grammar.Res
import com.cbcds.polishpal.shared.core.grammar.label_number_plural
import com.cbcds.polishpal.shared.core.grammar.label_number_singular
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import kotlin.math.max
import kotlin.math.min

private const val FORMS_DELIMITER = "\n"

private const val PRONOUN_HORIZONTAL_PADDING = 4
private const val FORM_HORIZONTAL_PADDING = 12

@Composable
internal fun FormsTable(
    minWidth: Dp,
    forms: ImmutableList<Form>,
    accentColor: Color,
    onAccentColor: Color,
    modifier: Modifier = Modifier,
) {
    val pronounColumnWidth = getPronounColumnWidth()
    val formColumnWidth = forms.getFormColumnWidth(
        minWidth = minWidth / 2 - pronounColumnWidth,
    )
    val width = 2 * (pronounColumnWidth + formColumnWidth)

    val scrollable = width > minWidth
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
            .defaultMinSize(minWidth = minWidth)
            .let {
                if (scrollable) it.horizontalScroll(scrollState) else it
            },
    ) {
        TableHeader(width)
        TableContent(
            forms = forms,
            accentColor = accentColor,
            onAccentColor = onAccentColor,
            pronounColumnWidth = pronounColumnWidth,
            formColumnWidth = formColumnWidth,
        )
    }

    if (scrollable) {
        HorizontalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState),
            style = defaultScrollbarStyle().copy(
                thickness = 4.dp,
                hideWhenIdle = false,
            ),
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}

@Composable
private fun TableHeader(
    width: Dp,
) {
    Row(Modifier.width(width)) {
        listOf(Number.SINGULAR, Number.PLURAL).forEach { number ->
            Text(
                text = number.toHeader(),
                style = AppTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
            )
        }
    }
}

@Composable
private fun TableContent(
    forms: ImmutableList<Form>,
    accentColor: Color,
    onAccentColor: Color,
    pronounColumnWidth: Dp,
    formColumnWidth: Dp,
) {
    val groupedForms by remember { mutableStateOf(forms.groupByNumber()) }

    val singulars = groupedForms[Number.SINGULAR].orEmpty()
    val plurals = groupedForms[Number.PLURAL].orEmpty()
    val rowsNum = min(singulars.size, plurals.size)

    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(16.dp)),
    ) {
        for (i in 0 until rowsNum) {
            Row(Modifier.height(IntrinsicSize.Min)) {
                listOf(singulars[i], plurals[i]).forEach { form ->
                    FormRow(
                        form = form,
                        accentColor = accentColor,
                        onAccentColor = onAccentColor,
                        pronounColumnWidth = pronounColumnWidth,
                        formWidth = formColumnWidth,
                        modifier = Modifier.fillMaxHeight(),
                    )
                }
            }
        }
    }
}

@Composable
private fun FormRow(
    form: Form?,
    accentColor: Color,
    onAccentColor: Color,
    pronounColumnWidth: Dp,
    formWidth: Dp,
    modifier: Modifier = Modifier,
) {
    Row(modifier.height(IntrinsicSize.Min)) {
        Text(
            text = form?.getPronounLabel().orEmpty(),
            color = onAccentColor,
            textAlign = TextAlign.Center,
            style = AppTheme.typography.bodySmall,
            modifier = Modifier
                .background(accentColor)
                .width(pronounColumnWidth)
                .padding(horizontal = PRONOUN_HORIZONTAL_PADDING.dp, vertical = 6.dp)
                .fillMaxHeight(),
        )
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .background(AppTheme.colorScheme.surfaceContainerLowest)
                .width(formWidth)
                .padding(horizontal = FORM_HORIZONTAL_PADDING.dp, vertical = 6.dp)
                .fillMaxHeight(),
        ) {
            Text(
                text = form?.values?.joinToString(FORMS_DELIMITER).orEmpty(),
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.onSurface,
            )
        }
    }
}

private fun ImmutableList<Form>.groupByNumber(): Map<Number, Array<Form?>> {
    val singularSize = count { it.gender.number == Number.SINGULAR }
    val size = max(singularSize, size - singularSize)
    val singulars = arrayOfNulls<Form?>(size)
    val plurals = arrayOfNulls<Form?>(size)

    var singularIndex = 0
    forEachIndexed { index, form ->
        when (form.gender.number) {
            Number.SINGULAR -> {
                singulars[singularIndex] = form
                ++singularIndex
            }
            Number.PLURAL -> {
                plurals[index - singularIndex] = form
            }
        }
    }

    return mapOf(Number.SINGULAR to singulars, Number.PLURAL to plurals)
}

@Composable
private fun getPronounColumnWidth(): Dp {
    return getMaxPronounLabelWidth() + (2 * PRONOUN_HORIZONTAL_PADDING).dp
}

@Composable
private fun ImmutableList<Form>.getFormColumnWidth(minWidth: Dp): Dp {
    var longestFormWidth = 0.dp

    val maxCharNum = maxOf { forms ->
        forms.values.maxOf { it.length }
    }

    for (forms in this) {
        for (form in forms.values) {
            if (form.length in maxCharNum - 1..maxCharNum) {
                val width = measureTextWidth(form, AppTheme.typography.bodyMedium)
                longestFormWidth = max(width, longestFormWidth)
            }
        }
    }

    return max(minWidth, longestFormWidth + (2 * FORM_HORIZONTAL_PADDING).dp)
}

@Composable
private fun Number.toHeader(): String {
    return when (this) {
        Number.SINGULAR -> stringResource(Res.string.label_number_singular)
        Number.PLURAL -> stringResource(Res.string.label_number_plural)
    }
}
