package com.cbcds.polishpal.feature.exercises.screen.exercise

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.feature.exercises.model.FormCheckResult

private const val FORM_REGEX = "[a-zA-ząćęłńóśźżĄĆĘŁŃÓŚŹŻ\\s]*"

private const val EMPTY_FORM = "___"
private const val FORM_DELIMITER = ", "

@Composable
internal fun EnterFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
) {
    val pattern = remember { Regex(FORM_REGEX) }

    TextField(
        value = value,
        onValueChange = { newText ->
            if (pattern.matches(newText)) {
                onValueChange(newText)
            }
        },
        singleLine = true,
        shape = RectangleShape,
        colors = formTextFieldColors(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Text,
            showKeyboardOnFocus = true,
            imeAction = imeAction,
        ),
        modifier = modifier,
    )
}

@Composable
internal fun CheckedFormTextField(
    originalValue: String,
    formCheckResult: FormCheckResult,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = TextFieldValue(
            annotatedString = getCheckedFormText(originalValue, formCheckResult),
        ),
        onValueChange = { },
        readOnly = true,
        singleLine = true,
        shape = RectangleShape,
        colors = formTextFieldColors(),
        modifier = modifier,
    )
}

@Composable
private fun getCheckedFormText(
    originalValue: String,
    formCheckResult: FormCheckResult,
): AnnotatedString {
    fun List<String>.toDisplayString() = joinToString(FORM_DELIMITER)

    return buildAnnotatedString {
        appendOriginalValue(originalValue, formCheckResult)

        when (formCheckResult) {
            is FormCheckResult.Correct -> {
                formCheckResult.otherCorrectForms.takeIf { it.isNotEmpty() }?.let { forms ->
                    append(" (${forms.toDisplayString()})")
                }
            }
            is FormCheckResult.Typo -> {
                append("  →  ${formCheckResult.formWithoutTypo}")
                formCheckResult.otherCorrectForms.takeIf { it.isNotEmpty() }?.let { forms ->
                    append(" (${forms.toDisplayString()})")
                }
            }
            is FormCheckResult.Error -> {
                formCheckResult.correctForms.takeIf { it.isNotEmpty() }?.let { forms ->
                    append("  →  ${forms.toDisplayString()}")
                }
            }
        }
    }
}

@Composable
private fun AnnotatedString.Builder.appendOriginalValue(
    originalValue: String,
    formCheckResult: FormCheckResult,
) {
    val color = when (formCheckResult) {
        is FormCheckResult.Correct -> AppTheme.extendedColorScheme.correct
        is FormCheckResult.Typo -> AppTheme.extendedColorScheme.semiCorrect
        is FormCheckResult.Error -> AppTheme.colorScheme.error
    }

    withStyle(SpanStyle(color)) {
        if (formCheckResult is FormCheckResult.Error.Blank) {
            append(EMPTY_FORM)
        } else {
            append(originalValue)
        }
    }
}

@Composable
private fun formTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors().copy(
        disabledTextColor = AppTheme.colorScheme.onSurface,
        focusedContainerColor = AppTheme.colorScheme.surfaceContainerLowest,
        unfocusedContainerColor = AppTheme.colorScheme.surfaceContainerLowest,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    )
}
