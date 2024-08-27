package com.cbcds.polishpal.feature.vocabulary.screen.word

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.component.TopAppBar
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.data.model.words.Verb
import com.cbcds.polishpal.feature.grammar.component.FavoriteIcon
import com.cbcds.polishpal.shared.core.ui.Res
import com.cbcds.polishpal.shared.core.ui.ic_info_outline
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WordTopAppBar(
    word: Verb,
    onInfoClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onBackClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    TopAppBar(
        title = {
            Text(
                text = word.infinitive,
                style = AppTheme.typography.titleLarge,
            )
        },
        onBackClick = onBackClick,
        actions = {
            IconButton(onClick = onInfoClick) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_info_outline),
                    tint = AppTheme.extendedColorScheme.accent2.color,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            }
            IconButton(onClick = onFavoriteClick) {
                FavoriteIcon(favorite = word.favorite)
            }
        },
        scrollBehavior = scrollBehavior,
    )
}
