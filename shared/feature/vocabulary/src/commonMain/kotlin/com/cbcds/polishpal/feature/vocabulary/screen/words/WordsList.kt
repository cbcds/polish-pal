package com.cbcds.polishpal.feature.vocabulary.screen.words

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.feature.grammar.component.FavoriteIcon

@Composable
internal fun WordsList(
    words: List<Verb>,
    onWordClick: (Verb) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize()) {
        val state = rememberLazyListState()

        LazyColumn(state = state) {
            itemsIndexed(words) { index, word ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .clickable(
                            role = Role.Button,
                            onClick = { onWordClick(word) }
                        )
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = word.infinitive,
                        style = AppTheme.typography.bodyLarge,
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                    )

                    if (word.favorite) {
                        FavoriteIcon(favorite = true, size = 20.dp)
                    }
                }

                if (index < words.lastIndex) {
                    HorizontalDivider()
                }
            }
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(state),
            style = defaultScrollbarStyle().copy(
                thickness = 4.dp,
                unhoverColor = AppTheme.colorScheme.outline.copy(alpha = 0.4f),
                hideWhenIdle = true,
            ),
            interactionSource = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
                .fillMaxHeight(),
        )
    }
}
