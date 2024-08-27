package com.cbcds.polishpal.core.ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable

@Composable
fun SlideHorizontallyTransition(
    visibleState: MutableTransitionState<Boolean>,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter = slideInHorizontally(initialOffsetX = { -it }),
        exit = slideOutHorizontally()
    ) {
        content()
    }
}
