package com.cbcds.polishpal.feature.grammar.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.shared.core.ui.Res
import com.cbcds.polishpal.shared.core.ui.ic_star_fill
import com.cbcds.polishpal.shared.core.ui.ic_star_otline
import org.jetbrains.compose.resources.vectorResource

@Composable
fun FavoriteIcon(
    favorite: Boolean,
    size: Dp = 24.dp,
) {
    if (favorite) {
        Icon(
            imageVector = vectorResource(Res.drawable.ic_star_fill),
            tint = AppTheme.extendedColorScheme.favorite,
            contentDescription = null,
            modifier = Modifier.size(size),
        )
    } else {
        Icon(
            imageVector = vectorResource(Res.drawable.ic_star_otline),
            tint = AppTheme.colorScheme.outline,
            contentDescription = null,
            modifier = Modifier.size(size),
        )
    }
}
