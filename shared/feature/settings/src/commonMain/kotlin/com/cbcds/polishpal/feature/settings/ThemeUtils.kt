package com.cbcds.polishpal.feature.settings

import com.cbcds.polishpal.data.model.Theme
import com.cbcds.polishpal.shared.feature.settings.Res
import com.cbcds.polishpal.shared.feature.settings.theme_dark
import com.cbcds.polishpal.shared.feature.settings.theme_light
import com.cbcds.polishpal.shared.feature.settings.theme_system
import org.jetbrains.compose.resources.StringResource

internal fun Theme.toStringResource(): StringResource {
    return when (this) {
        Theme.LIGHT -> Res.string.theme_light
        Theme.DARK -> Res.string.theme_dark
        Theme.SYSTEM -> Res.string.theme_system
    }
}
