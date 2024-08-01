package com.cbcds.polishpal.feature.vocabulary.screen.words

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.utils.clearFocusOnKeyboardDismiss
import com.cbcds.polishpal.shared.core.ui.ic_close_circle_outline
import com.cbcds.polishpal.shared.core.ui.ic_search_outline
import com.cbcds.polishpal.shared.feature.vocabulary.Res
import com.cbcds.polishpal.shared.feature.vocabulary.find_word
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import com.cbcds.polishpal.shared.core.ui.Res as uiRes

@Composable
internal fun WordSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    TextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        label = if (!focused && query.isEmpty()) {
            { Text(stringResource(Res.string.find_word)) }
        } else {
            null
        },
        leadingIcon = {
            Icon(
                imageVector = vectorResource(uiRes.drawable.ic_search_outline),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
        trailingIcon = if (query.isNotEmpty()) {
            {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = vectorResource(uiRes.drawable.ic_close_circle_outline),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done,
        ),
        shape = RoundedCornerShape(28.dp),
        colors = getWordSearchBarColors(),
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .clearFocusOnKeyboardDismiss(),
    )
}

@Composable
private fun getWordSearchBarColors(): TextFieldColors {
    return TextFieldDefaults.colors().copy(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
    )
}
