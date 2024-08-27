package com.cbcds.polishpal.core.ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable

@Composable
fun SlideVerticallyTransition(
    visibleState: MutableTransitionState<Boolean>,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically()
    ) {
        content()
    }
}
