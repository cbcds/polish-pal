package com.cbcds.polishpal.feature.settings.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.shared.core.ui.Res
import com.cbcds.polishpal.shared.core.ui.ic_arrow_down_fill
import org.jetbrains.compose.resources.vectorResource

@Composable
internal fun SettingsDropDownHeader(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(
                role = Role.DropdownList,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(vertical = 6.dp)
    ) {
        val contentColor =
            if (enabled) AppTheme.colorScheme.onSurface else AppTheme.colorScheme.outline
        Text(
            text = text,
            style = AppTheme.typography.labelLarge,
            color = contentColor,
        )
        Icon(
            imageVector = vectorResource(Res.drawable.ic_arrow_down_fill),
            tint = contentColor,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 12.dp)
                .size(12.dp),
        )
    }
}
