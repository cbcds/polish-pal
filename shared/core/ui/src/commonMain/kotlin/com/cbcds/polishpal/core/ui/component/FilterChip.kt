package com.cbcds.polishpal.core.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.shared.core.ui.Res
import com.cbcds.polishpal.shared.core.ui.ic_arrow_down_fill
import com.cbcds.polishpal.shared.core.ui.ic_checkmark_outline
import org.jetbrains.compose.resources.vectorResource

@Composable
fun CheckableFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    FilterChip(
        selected = selected,
        enabled = enabled,
        label = {
            Text(
                text = label,
                maxLines = 1,
            )
        },
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_checkmark_outline),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
            }
        } else {
            null
        },
        onClick = onClick,
    )
}

@Composable
fun DropdownFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    FilterChip(
        selected = selected,
        enabled = enabled,
        label = {
            Text(
                text = label,
                maxLines = 1,
            )
        },
        trailingIcon = {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_arrow_down_fill),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
            )
        },
        onClick = onClick,
    )
}
