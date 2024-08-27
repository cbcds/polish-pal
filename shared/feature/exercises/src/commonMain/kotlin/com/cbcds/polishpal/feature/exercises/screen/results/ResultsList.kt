package com.cbcds.polishpal.feature.exercises.screen.results

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.component.ElevatedCard
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.feature.grammar.component.FavoriteIcon
import kotlin.math.roundToInt

private const val LOW_RESULT_LIMIT = 51
private const val MEDIUM_RESULT_LIMIT = 80

@Composable
internal fun ResultsList(
    verbToResult: LinkedHashMap<Verb, Float>,
    onFavoriteClick: (verbId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        for ((verb, result) in verbToResult) {
            ResultItem(
                verb = verb,
                result = result,
                onFavoriteClick = { onFavoriteClick(verb.id) },
            )
        }
    }
}

@Composable
private fun ResultItem(
    verb: Verb,
    result: Float,
    onFavoriteClick: () -> Unit,
) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = verb.infinitive,
                style = AppTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
                    .padding(start = 16.dp),
            )

            @Suppress("MagicNumber")
            Text(
                text = resultPercentToString((result * 100).roundToInt()),
                style = AppTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 16.dp),
            )

            IconButton(onClick = onFavoriteClick) {
                FavoriteIcon(favorite = verb.favorite)
            }
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun resultPercentToString(
    @IntRange(0, 100) result: Int,
): AnnotatedString {
    return buildAnnotatedString {
        val color = when {
            result < LOW_RESULT_LIMIT -> AppTheme.colorScheme.error
            result < MEDIUM_RESULT_LIMIT -> AppTheme.extendedColorScheme.semiCorrect
            else -> AppTheme.extendedColorScheme.correct
        }

        withStyle(SpanStyle(color)) {
            append("$result%")
        }
    }
}
