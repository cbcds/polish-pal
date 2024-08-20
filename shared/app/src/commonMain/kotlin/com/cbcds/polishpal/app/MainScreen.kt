package com.cbcds.polishpal.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.component.ElevatedCard
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.data.model.exercises.ExerciseType
import com.cbcds.polishpal.feature.exercises.screen.start.StartExerciseDialog
import com.cbcds.polishpal.shared.app.Res
import com.cbcds.polishpal.shared.app.hero
import com.cbcds.polishpal.shared.app.main_screen_title
import com.cbcds.polishpal.shared.core.grammar.title_mood_conditional
import com.cbcds.polishpal.shared.core.grammar.title_mood_imperative
import com.cbcds.polishpal.shared.core.grammar.title_mood_indicative
import com.cbcds.polishpal.shared.core.ui.ic_clock_fill
import com.cbcds.polishpal.shared.core.ui.ic_message_circle_fill
import com.cbcds.polishpal.shared.core.ui.ic_question_fill
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import com.cbcds.polishpal.shared.core.grammar.Res as gramRes
import com.cbcds.polishpal.shared.core.ui.Res as uiRes

@Composable
internal fun MainScreen() {
    var selectedExerciseType by rememberSaveable { mutableStateOf<ExerciseType?>(null) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(20.dp),
    ) {
        Header(Modifier.weight(1f))
        Spacer(Modifier.height(28.dp))
        LearningModeCards(
            onExerciseClick = { selectedExerciseType = it },
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(2f),
        )
    }

    selectedExerciseType?.let { exerciseType ->
        StartExerciseDialog(
            exerciseType = exerciseType,
            onDismiss = { selectedExerciseType = null },
        )
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(Res.string.main_screen_title),
            style = AppTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        )
        Image(
            painter = painterResource(Res.drawable.hero),
            contentDescription = null,
            modifier = Modifier.height(132.dp),
        )
    }
}

@Composable
private fun LearningModeCards(
    onExerciseClick: (ExerciseType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        LearningModeCard(
            titleRes = gramRes.string.title_mood_indicative,
            iconRes = uiRes.drawable.ic_clock_fill,
            iconColor = AppTheme.extendedColorScheme.accent1.color,
            iconBackgroundColor = AppTheme.extendedColorScheme.accent1.colorContainer,
            onClick = { onExerciseClick(ExerciseType.INDICATIVE_MOOD) },
        )

        LearningModeCard(
            titleRes = gramRes.string.title_mood_imperative,
            iconRes = uiRes.drawable.ic_message_circle_fill,
            iconColor = AppTheme.extendedColorScheme.accent2.color,
            iconBackgroundColor = AppTheme.extendedColorScheme.accent2.colorContainer,
            onClick = { onExerciseClick(ExerciseType.IMPERATIVE_MOOD) },
        )

        LearningModeCard(
            titleRes = gramRes.string.title_mood_conditional,
            iconRes = uiRes.drawable.ic_question_fill,
            iconColor = AppTheme.extendedColorScheme.accent3.color,
            iconBackgroundColor = AppTheme.extendedColorScheme.accent3.colorContainer,
            onClick = { onExerciseClick(ExerciseType.CONDITIONAL_MOOD) },
        )
    }
}

@Composable
private fun LearningModeCard(
    titleRes: StringResource,
    iconRes: DrawableResource,
    iconColor: Color,
    iconBackgroundColor: Color,
    onClick: () -> Unit,
) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(
                    role = Role.Button,
                    onClick = onClick,
                )
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor),
            ) {
                Icon(
                    imageVector = vectorResource(iconRes),
                    tint = iconColor,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }

            Text(
                text = stringResource(titleRes),
                style = AppTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 12.dp),
            )
        }
    }
}
