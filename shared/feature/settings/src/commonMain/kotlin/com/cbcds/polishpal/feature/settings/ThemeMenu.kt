package com.cbcds.polishpal.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cbcds.polishpal.data.model.settings.Theme
import com.cbcds.polishpal.shared.feature.settings.Res
import com.cbcds.polishpal.shared.feature.settings.theme_dark
import com.cbcds.polishpal.shared.feature.settings.theme_light
import com.cbcds.polishpal.shared.feature.settings.theme_system
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ThemeMenu(
    currentTheme: Theme,
    onThemeChange: (Theme) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        SettingsDropDownHeader(
            text = stringResource(currentTheme.toStringResource()),
            onClick = { expanded = true },
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            for (theme in Theme.entries) {
                DropdownMenuItem(
                    text = { Text(stringResource(theme.toStringResource())) },
                    onClick = {
                        expanded = false
                        onThemeChange(theme)
                    }
                )
            }
        }
    }
}

private fun Theme.toStringResource(): StringResource {
    return when (this) {
        Theme.LIGHT -> Res.string.theme_light
        Theme.DARK -> Res.string.theme_dark
        Theme.SYSTEM -> Res.string.theme_system
    }
}
