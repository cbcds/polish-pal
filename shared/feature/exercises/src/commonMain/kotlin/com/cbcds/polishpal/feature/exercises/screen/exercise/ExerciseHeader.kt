package com.cbcds.polishpal.feature.exercises.screen.exercise

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.data.model.exercises.Exercise
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.feature.grammar.component.FavoriteIcon
import com.cbcds.polishpal.feature.grammar.toTitleStringResource
import com.cbcds.polishpal.shared.core.ui.Res
import com.cbcds.polishpal.shared.core.ui.ic_info_outline
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
internal fun ExerciseHeader(
    verb: Verb,
    exercise: Exercise,
    onInfoClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.Center),
            ) {
                Text(
                    text = verb.infinitive,
                    style = AppTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                )
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 32.dp) {
                    IconButton(onClick = onInfoClick) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_info_outline),
                            tint = AppTheme.extendedColorScheme.accent2.color,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }

            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
            ) {
                FavoriteIcon(favorite = verb.favorite)
            }
        }

        if (exercise is Exercise.IndicativeMood) {
            val text = "${exercise.tenseNumber}/${exercise.numberOfTenses}: " +
                stringResource(exercise.tense.toTitleStringResource())
            Text(
                text = text,
                style = AppTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    }
}
