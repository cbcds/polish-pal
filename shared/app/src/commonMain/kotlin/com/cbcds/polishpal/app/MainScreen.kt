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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.component.ElevatedCard
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.shared.app.Res
import com.cbcds.polishpal.shared.app.hero
import com.cbcds.polishpal.shared.app.learning_mode_conditional
import com.cbcds.polishpal.shared.app.learning_mode_imperative
import com.cbcds.polishpal.shared.app.learning_mode_indicative
import com.cbcds.polishpal.shared.app.main_screen_title
import com.cbcds.polishpal.shared.core.ui.ic_clock_fill
import com.cbcds.polishpal.shared.core.ui.ic_message_circle_fill
import com.cbcds.polishpal.shared.core.ui.ic_options_outline
import com.cbcds.polishpal.shared.core.ui.ic_question_fill
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import com.cbcds.polishpal.shared.core.ui.Res as uiRes

@Composable
internal fun MainScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(20.dp),
    ) {
        Header(Modifier.weight(1f))
        Spacer(Modifier.height(28.dp))
        LearningModeCards(
            Modifier
                .verticalScroll(rememberScrollState())
                .weight(2f),
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
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        LearningModeCard(
            titleRes = Res.string.learning_mode_indicative,
            iconRes = uiRes.drawable.ic_clock_fill,
            iconColor = AppTheme.extendedColorScheme.accent2.color,
            iconBackgroundColor = AppTheme.extendedColorScheme.accent2.colorContainer,
            showSettings = true,
            onSettingsClick = {},
            onClick = {},
        )

        LearningModeCard(
            titleRes = Res.string.learning_mode_imperative,
            iconRes = uiRes.drawable.ic_message_circle_fill,
            iconColor = AppTheme.extendedColorScheme.accent1.color,
            iconBackgroundColor = AppTheme.extendedColorScheme.accent1.colorContainer,
            onClick = {},
        )

        LearningModeCard(
            titleRes = Res.string.learning_mode_conditional,
            iconRes = uiRes.drawable.ic_question_fill,
            iconColor = AppTheme.extendedColorScheme.accent3.color,
            iconBackgroundColor = AppTheme.extendedColorScheme.accent3.colorContainer,
            onClick = {},
        )
    }
}

@Composable
private fun LearningModeCard(
    titleRes: StringResource,
    iconRes: DrawableResource,
    iconColor: Color,
    iconBackgroundColor: Color,
    showSettings: Boolean = false,
    onSettingsClick: () -> Unit = {},
    onClick: () -> Unit,
) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        role = Role.Button,
                        onClick = onClick,
                    )
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

            if (showSettings) {
                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier.padding(horizontal = 8.dp),
                ) {
                    Icon(
                        imageVector = vectorResource(uiRes.drawable.ic_options_outline),
                        tint = iconColor,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }
    }
}
