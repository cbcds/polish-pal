package com.cbcds.polishpal.feature.exercises.screen.start

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.animation.ShakeAnimation
import com.cbcds.polishpal.core.ui.theme.AppTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingHeader(
    titleRes: StringResource,
    showError: Boolean = false,
    showErrorAnimation: Boolean = false,
    onErrorAnimationEnd: (() -> Unit)? = null,
) {
    val text = buildAnnotatedString {
        append(stringResource(titleRes))
        if (showError) {
            withStyle(SpanStyle(AppTheme.colorScheme.error)) {
                append(" *")
            }
        }
    }
    val offset = remember { Animatable(0f) }

    Text(
        text = text,
        style = AppTheme.typography.titleSmall,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .offset { IntOffset(offset.value.dp.roundToPx(), 0) },
    )

    if (showErrorAnimation) {
        LaunchedEffect(true) {
            offset.animateTo(targetValue = 0f, animationSpec = ShakeAnimation().keyframes)
            onErrorAnimationEnd?.invoke()
        }
    }
}
