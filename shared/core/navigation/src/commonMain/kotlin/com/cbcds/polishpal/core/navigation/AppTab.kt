package com.cbcds.polishpal.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

abstract class AppTab : AppScreen, Tab {

    abstract val index: UShort
    abstract val titleRes: StringResource
    abstract val unselectedIconRes: DrawableResource
    abstract val selectedIconRes: DrawableResource

    val appTabOptions: AppTabOptions
        @Composable get() = getTabOptions()

    override val options: TabOptions
        @Composable get() {
            val index = appTabOptions.index
            val title = appTabOptions.title

            return remember {
                TabOptions(index = index, title = title)
            }
        }

    override val fullscreen = false

    @Composable
    private fun getTabOptions(): AppTabOptions {
        val title = stringResource(titleRes)
        val unselectedIcon = rememberVectorPainter(vectorResource(unselectedIconRes))
        val selectedIcon = rememberVectorPainter(vectorResource(selectedIconRes))

        val appTabOptions = AppTabOptions(
            index = index,
            title = title,
            unselectedIcon = unselectedIcon,
            selectedIcon = selectedIcon,
        )

        return remember { appTabOptions }
    }
}

data class AppTabOptions(
    val index: UShort,
    val title: String,
    val unselectedIcon: Painter,
    val selectedIcon: Painter,
)
