package com.cbcds.polishpal.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

abstract class AppTab : AppScreen, Tab {

    override val fullscreen = false

    @Composable
    protected fun getTabOptions(
        index: UShort,
        titleRes: StringResource,
        iconRes: DrawableResource,
    ): TabOptions {
        val title = stringResource(titleRes)
        val icon = rememberVectorPainter(vectorResource(iconRes))

        return remember {
            TabOptions(
                index = index,
                title = title,
                icon = icon,
            )
        }
    }
}
