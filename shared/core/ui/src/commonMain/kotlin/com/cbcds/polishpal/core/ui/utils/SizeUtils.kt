package com.cbcds.polishpal.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp

@Composable
fun Float.toDp(): Dp {
    return with(LocalDensity.current) { this@toDp.toDp() }
}

@Composable
fun Int.toDp(): Dp {
    return with(LocalDensity.current) { this@toDp.toDp() }
}

@Composable
fun Dp.toPx(): Float {
    return with(LocalDensity.current) { this@toPx.toPx() }
}

@Composable
fun Dp.roundToPx(): Int {
    return with(LocalDensity.current) { this@roundToPx.roundToPx() }
}

@Composable
fun measureTextWidth(text: String, style: TextStyle): Dp {
    val textMeasurer = rememberTextMeasurer()
    val widthInPixels = textMeasurer.measure(text, style).size.width

    return widthInPixels.toDp()
}
