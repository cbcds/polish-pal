package com.cbcds.polishpal.feature.exercises.screen.exercise

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.component.ElevatedCard
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.data.model.words.Form
import com.cbcds.polishpal.feature.exercises.model.FormCheckResult
import com.cbcds.polishpal.feature.grammar.getMaxPronounLabelWidth
import com.cbcds.polishpal.feature.grammar.getPronounLabel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

private const val PRONOUN_HORIZONTAL_PADDING = 4

@Composable
internal fun FormsTable(
    forms: ImmutableList<Form>,
    expectedToActualForms: ImmutableMap<Form, MutableState<String>>,
    formsCheckResult: ImmutableMap<Form, FormCheckResult>?,
) {
    val pronounColumnWidth = getPronounColumnWidth()

    TableContent(
        forms = forms,
        expectedToActualForms = expectedToActualForms,
        formsCheckResult = formsCheckResult,
        pronounColumnWidth = pronounColumnWidth,
    )
}

@Composable
private fun TableContent(
    forms: ImmutableList<Form>,
    expectedToActualForms: ImmutableMap<Form, MutableState<String>>,
    formsCheckResult: ImmutableMap<Form, FormCheckResult>?,
    pronounColumnWidth: Dp,
) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        forms.forEachIndexed { index, form ->
            FormRow(
                form = form,
                enteredForm = expectedToActualForms[form] ?: return@forEachIndexed,
                formCheckResult = formsCheckResult?.get(form),
                pronounColumnWidth = pronounColumnWidth,
                imeAction = if (index < forms.lastIndex) ImeAction.Next else ImeAction.Done,
            )
            if (index != forms.lastIndex) {
                HorizontalDivider(Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun FormRow(
    form: Form?,
    enteredForm: MutableState<String>,
    formCheckResult: FormCheckResult?,
    pronounColumnWidth: Dp,
    imeAction: ImeAction,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(IntrinsicSize.Min),
    ) {
        Text(
            text = form?.getPronounLabel().orEmpty(),
            textAlign = TextAlign.Center,
            style = AppTheme.typography.bodySmall,
            modifier = Modifier
                .width(pronounColumnWidth)
                .padding(horizontal = PRONOUN_HORIZONTAL_PADDING.dp, vertical = 6.dp),
        )

        VerticalDivider()

        if (formCheckResult == null) {
            EnterFormTextField(
                value = enteredForm.value,
                onValueChange = { enteredForm.value = it },
                imeAction = imeAction,
                modifier = Modifier.weight(1f),
            )
        } else {
            CheckedFormTextField(
                originalValue = enteredForm.value,
                formCheckResult = formCheckResult,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun getPronounColumnWidth(): Dp {
    return getMaxPronounLabelWidth() + (2 * PRONOUN_HORIZONTAL_PADDING).dp
}
