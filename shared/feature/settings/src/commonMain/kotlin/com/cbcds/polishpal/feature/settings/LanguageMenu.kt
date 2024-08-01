package com.cbcds.polishpal.feature.settings

import androidx.compose.runtime.Composable
import com.cbcds.polishpal.data.model.settings.Locale

@Composable
internal expect fun LanguageMenu(
    currentLocale: Locale,
    onLocaleChange: (Locale) -> Unit,
)
