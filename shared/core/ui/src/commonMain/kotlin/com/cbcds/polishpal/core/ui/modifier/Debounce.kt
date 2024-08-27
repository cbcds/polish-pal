package com.cbcds.polishpal.core.ui.modifier

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlin.time.TimeSource

@Composable
inline fun debounced(
    crossinline onClick: () -> Unit,
    debounceTime: Long = 500L,
): () -> Unit {
    var lastClickTime by remember { mutableStateOf(TimeSource.Monotonic.markNow()) }
    val onClickLambda: () -> Unit = {
        val now = TimeSource.Monotonic.markNow()
        if ((now - lastClickTime).inWholeMilliseconds > debounceTime) {
            onClick()
        }
        lastClickTime = now
    }
    return onClickLambda
}

fun Modifier.debouncedClickable(
    debounceTime: Long = 1000L,
    onClick: () -> Unit
): Modifier {
    return composed {
        val clickable = debounced(debounceTime = debounceTime, onClick = onClick)
        clickable { clickable() }
    }
}
