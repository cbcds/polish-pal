package com.cbcds.polishpal.core.ui.theme

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

private val DefaultLightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val DefaultDarkScrim = Color.argb(0x80, 0x1B, 0x1B, 0x1B)

@Composable
internal actual fun applyThemeToSystemElements(darkTheme: Boolean) {
    val activity = LocalContext.current as? ComponentActivity ?: return

    activity.enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.auto(
            lightScrim = Color.TRANSPARENT,
            darkScrim = Color.TRANSPARENT,
            detectDarkMode = { darkTheme }
        ),
        navigationBarStyle = SystemBarStyle.auto(
            lightScrim = DefaultLightScrim,
            darkScrim = DefaultDarkScrim,
            detectDarkMode = { darkTheme }
        ),
    )

    activity.window.navigationBarColor =
        (if (darkTheme) surfaceContainerDark else surfaceContainerLight).toArgb()
}
