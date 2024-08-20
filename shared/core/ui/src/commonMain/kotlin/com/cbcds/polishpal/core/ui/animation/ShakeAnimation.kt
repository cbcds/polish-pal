package com.cbcds.polishpal.core.ui.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.keyframes

class ShakeAnimation {

    @Suppress("MagicNumber")
    val keyframes: AnimationSpec<Float> = keyframes {
        durationMillis = 800
        val easing = FastOutLinearInEasing

        for (i in 1..8) {
            val x = when (i % 3) {
                0 -> 3f
                1 -> -3f
                else -> 0f
            }
            x at durationMillis / 20 * i using easing
        }
    }
}
