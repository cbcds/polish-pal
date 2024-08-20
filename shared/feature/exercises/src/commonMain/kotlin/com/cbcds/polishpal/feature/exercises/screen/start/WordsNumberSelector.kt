package com.cbcds.polishpal.feature.exercises.screen.start

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.theme.AppTheme
import kotlin.math.max
import kotlin.math.min

@Composable
internal fun WordsNumberSelector(
    state: WordsNumberState,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        OutlinedIconButton(
            enabled = state.canDecrement(),
            shape = RoundedCornerShape(32.dp),
            onClick = state::tryDecrement,
        ) {
            Text("-")
        }

        Text(
            text = state.value.toString(),
            style = AppTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        OutlinedIconButton(
            enabled = state.canIncrement(),
            shape = RoundedCornerShape(32.dp),
            onClick = state::tryIncrement,
        ) {
            Text("+")
        }
    }
}

@Composable
internal fun rememberWordsNumberState(initialWordsNumber: Int): WordsNumberState {
    val state = rememberSaveable(saver = WordsNumberState.Saver()) {
        WordsNumberState(initialWordsNumber)
    }

    return state
}

internal class WordsNumberState(initialValue: Int) {
    init {
        require(initialValue in MIN_WORDS_NUMBER..MAX_WORDS_NUMBER) {
            "InitialValue should be in [$MIN_WORDS_NUMBER..$MAX_WORDS_NUMBER] range"
        }
    }

    private val valueState = mutableIntStateOf(initialValue)
    var value: Int
        get() = valueState.intValue
        private set(value) {
            valueState.intValue = value
        }

    fun canIncrement(): Boolean = value < MAX_WORDS_NUMBER

    fun canDecrement(): Boolean = value > MIN_WORDS_NUMBER

    fun tryIncrement() {
        value = min(value + 1, MAX_WORDS_NUMBER)
    }

    fun tryDecrement() {
        value = max(value - 1, MIN_WORDS_NUMBER)
    }

    companion object {

        private const val MIN_WORDS_NUMBER = 1
        private const val MAX_WORDS_NUMBER = 5

        fun Saver(): Saver<WordsNumberState, *> {
            return Saver(
                save = { it.value },
                restore = { value -> WordsNumberState(value) }
            )
        }
    }
}
