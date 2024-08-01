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
import com.cbcds.polishpal.data.model.settings.Locale

@Composable
internal actual fun LanguageMenu(
    currentLocale: Locale,
    onLocaleChange: (Locale) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        SettingsDropDownHeader(
            text = currentLocale.displayName,
            onClick = { expanded = true },
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            for (language in Locale.entries) {
                DropdownMenuItem(
                    text = { Text(language.displayName) },
                    onClick = {
                        expanded = false
                        onLocaleChange(language)
                    }
                )
            }
        }
    }
}
